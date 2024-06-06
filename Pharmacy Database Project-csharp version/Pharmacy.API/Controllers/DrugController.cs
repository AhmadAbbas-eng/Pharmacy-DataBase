using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class DrugController : ControllerBase
{
    private readonly DrugMapper _drugMapper;
    private readonly IDrugService _drugService;

    public DrugController(IDrugService drugService, DrugMapper drugMapper)
    {
        _drugService = drugService;
        _drugMapper = drugMapper;
    }

    [HttpGet("risk-category/{riskCategory}")]
    public async Task<IActionResult> GetByRiskCategory(char riskCategory)
    {
        var drugs = await _drugService.GetDrugsByRiskCategoryAsync(riskCategory);
        var drugDtos = drugs.Select(_drugMapper.ToDto);
        return Ok(drugDtos);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var drugs = await _drugService.GetAllDrugsAsync();
        var drugDtos = drugs.Select(_drugMapper.ToDto);
        return Ok(drugDtos);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var drug = await _drugService.GetDrugByIdAsync(id);
        if (drug == null) return NotFound();

        var drugDto = _drugMapper.ToDto(drug);
        return Ok(drugDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(DrugDto drugDto)
    {
        var drugDomain = _drugMapper.ToDomain(drugDto);
        var addedDrug = await _drugService.AddDrugAsync(drugDomain);
        var addedDrugDto = _drugMapper.ToDto(addedDrug);
        return CreatedAtAction(nameof(GetById), new { id = addedDrugDto.ProductId }, addedDrugDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, DrugDto drugDto)
    {
        if (id != drugDto.ProductId) return BadRequest();

        var drugDomain = _drugMapper.ToDomain(drugDto);
        var updatedDrug = await _drugService.UpdateDrugAsync(drugDomain);
        if (updatedDrug == null) return NotFound();

        var updatedDrugDto = _drugMapper.ToDto(updatedDrug);
        return Ok(updatedDrugDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _drugService.DeleteDrugAsync(id);
        if (!deleted) return NotFound();

        return NoContent();
    }
}