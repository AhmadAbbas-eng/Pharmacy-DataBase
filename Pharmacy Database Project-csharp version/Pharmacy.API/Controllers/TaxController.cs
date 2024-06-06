using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class TaxController : ControllerBase
{
    private readonly ITaxService _taxService;

    public TaxController(ITaxService taxService)
    {
        _taxService = taxService;
    }

    [HttpGet("by-date-range")]
    public async Task<IActionResult> GetByStartAndEndDate(DateTime startDate, DateTime endDate)
    {
        var taxes = await _taxService.GetTaxesByStartAndEndDateAsync(startDate, endDate);
        return Ok(taxes);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var taxes = await _taxService.GetAllTaxesAsync();
        return Ok(taxes);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var tax = await _taxService.GetTaxByIdAsync(id);
        if (tax == null) return NotFound();
        return Ok(tax);
    }

    [HttpPost]
    public async Task<IActionResult> Add(TaxDomain tax)
    {
        var addedTax = await _taxService.AddTaxAsync(tax);
        return CreatedAtAction(nameof(GetById), new { id = addedTax.Id }, addedTax);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(string id, TaxDomain tax)
    {
        if (id != tax.Id) return BadRequest();

        var updatedTax = await _taxService.UpdateTaxAsync(tax);
        if (updatedTax == null) return NotFound();
        return Ok(updatedTax);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _taxService.DeleteTaxAsync(id);
        if (!deleted) return NotFound();
        return NoContent();
    }
}