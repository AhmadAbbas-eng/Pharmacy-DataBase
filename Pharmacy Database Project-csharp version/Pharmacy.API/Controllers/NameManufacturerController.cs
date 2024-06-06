using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class NameManufacturerController : ControllerBase
{
    private readonly INameManufacturerService _nameManufacturerService;

    public NameManufacturerController(INameManufacturerService nameManufacturerService)
    {
        _nameManufacturerService = nameManufacturerService;
    }

    [HttpGet("distinct")]
    public async Task<IActionResult> GetDistinctNames()
    {
        var distinctNames = await _nameManufacturerService.GetDistinctNamesAsync();
        return Ok(distinctNames);
    }

    [HttpPut("update-name")]
    public async Task<IActionResult> UpdateName(string oldName, string newName)
    {
        await _nameManufacturerService.UpdateNameAsync(oldName, newName);
        return NoContent();
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var nameManufacturers = await _nameManufacturerService.GetAllNameManufacturersAsync();
        return Ok(nameManufacturers);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var nameManufacturer = await _nameManufacturerService.GetNameManufacturerByIdAsync(id);
        if (nameManufacturer == null) return NotFound();
        return Ok(nameManufacturer);
    }

    [HttpPost]
    public async Task<IActionResult> Add(NameManufacturerDomain nameManufacturer)
    {
        var addedNameManufacturer = await _nameManufacturerService.AddNameManufacturerAsync(nameManufacturer);
        return CreatedAtAction(nameof(GetById), new { id = addedNameManufacturer.Id }, addedNameManufacturer);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, NameManufacturerDomain nameManufacturer)
    {
        if (id != nameManufacturer.Id) return BadRequest();

        var updatedNameManufacturer = await _nameManufacturerService.UpdateNameManufacturerAsync(nameManufacturer);
        if (updatedNameManufacturer == null) return NotFound();
        return Ok(updatedNameManufacturer);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _nameManufacturerService.DeleteNameManufacturerAsync(id);
        if (!deleted) return NotFound();
        return NoContent();
    }
}