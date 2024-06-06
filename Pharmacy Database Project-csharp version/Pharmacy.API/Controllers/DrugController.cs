using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class DrugController : ControllerBase
{
    private readonly IDrugService _drugService;

    public DrugController(IDrugService drugService)
    {
        _drugService = drugService;
    }

    [HttpGet("risk-category/{riskCategory}")]
    public async Task<IActionResult> GetByRiskCategory(char riskCategory)
    {
        var drugs = await _drugService.GetDrugsByRiskCategoryAsync(riskCategory);
        return Ok(drugs);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var drugs = await _drugService.GetAllDrugsAsync();
        return Ok(drugs);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var drug = await _drugService.GetDrugByIdAsync(id);
        if (drug == null) return NotFound();
        return Ok(drug);
    }

    [HttpPost]
    public async Task<IActionResult> Add(DrugDomain drug)
    {
        var addedDrug = await _drugService.AddDrugAsync(drug);
        return CreatedAtAction(nameof(GetById), new { id = addedDrug.ProductId }, addedDrug);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, DrugDomain drug)
    {
        if (id != drug.ProductId) return BadRequest();

        var updatedDrug = await _drugService.UpdateDrugAsync(drug);
        if (updatedDrug == null) return NotFound();
        return Ok(updatedDrug);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _drugService.DeleteDrugAsync(id);
        if (!deleted) return NotFound();
        return NoContent();
    }
}