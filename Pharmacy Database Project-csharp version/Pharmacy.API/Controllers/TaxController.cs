using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class TaxController : ControllerBase
{
    private readonly TaxMapper _taxMapper;
    private readonly ITaxService _taxService;

    public TaxController(ITaxService taxService, TaxMapper taxMapper)
    {
        _taxService = taxService;
        _taxMapper = taxMapper;
    }

    [HttpGet("by-date-range")]
    public async Task<IActionResult> GetByStartAndEndDate(DateTime startDate, DateTime endDate)
    {
        var taxes = await _taxService.GetTaxesByStartAndEndDateAsync(startDate, endDate);
        var taxDtos = taxes.Select(_taxMapper.ToDto);
        return Ok(taxDtos);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var taxes = await _taxService.GetAllTaxesAsync();
        var taxDtos = taxes.Select(_taxMapper.ToDto);
        return Ok(taxDtos);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var tax = await _taxService.GetTaxByIdAsync(id);
        if (tax == null) return NotFound();

        var taxDto = _taxMapper.ToDto(tax);
        return Ok(taxDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(TaxDto taxDto)
    {
        var taxDomain = _taxMapper.ToDomain(taxDto);
        var addedTax = await _taxService.AddTaxAsync(taxDomain);
        var addedTaxDto = _taxMapper.ToDto(addedTax);
        return CreatedAtAction(nameof(GetById), new { id = addedTaxDto.Id }, addedTaxDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(string id, TaxDto taxDto)
    {
        if (id != taxDto.Id) return BadRequest();

        var taxDomain = _taxMapper.ToDomain(taxDto);
        var updatedTax = await _taxService.UpdateTaxAsync(taxDomain);
        if (updatedTax == null) return NotFound();

        var updatedTaxDto = _taxMapper.ToDto(updatedTax);
        return Ok(updatedTaxDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _taxService.DeleteTaxAsync(id);
        if (!deleted) return NotFound();

        return NoContent();
    }
}