using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class NameManufacturerController : ControllerBase
{
    private readonly NameManufacturerMapper _nameManufacturerMapper;
    private readonly INameManufacturerService _nameManufacturerService;

    public NameManufacturerController(INameManufacturerService nameManufacturerService,
        NameManufacturerMapper nameManufacturerMapper)
    {
        _nameManufacturerService = nameManufacturerService;
        _nameManufacturerMapper = nameManufacturerMapper;
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
        var nameManufacturerDtos = nameManufacturers.Select(_nameManufacturerMapper.ToDto);
        return Ok(nameManufacturerDtos);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var nameManufacturer = await _nameManufacturerService.GetNameManufacturerByIdAsync(id);
        if (nameManufacturer == null) return NotFound();

        var nameManufacturerDto = _nameManufacturerMapper.ToDto(nameManufacturer);
        return Ok(nameManufacturerDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(NameManufacturerDto nameManufacturerDto)
    {
        var nameManufacturerDomain = _nameManufacturerMapper.ToDomain(nameManufacturerDto);
        var addedNameManufacturer = await _nameManufacturerService.AddNameManufacturerAsync(nameManufacturerDomain);
        var addedNameManufacturerDto = _nameManufacturerMapper.ToDto(addedNameManufacturer);
        return CreatedAtAction(nameof(GetById), new { id = addedNameManufacturerDto.Id }, addedNameManufacturerDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, NameManufacturerDto nameManufacturerDto)
    {
        if (id != nameManufacturerDto.Id) return BadRequest();

        var nameManufacturerDomain = _nameManufacturerMapper.ToDomain(nameManufacturerDto);
        var updatedNameManufacturer =
            await _nameManufacturerService.UpdateNameManufacturerAsync(nameManufacturerDomain);
        if (updatedNameManufacturer == null) return NotFound();
        var updatedNameManufacturerDto = _nameManufacturerMapper.ToDto(updatedNameManufacturer);
        return Ok(updatedNameManufacturerDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _nameManufacturerService.DeleteNameManufacturerAsync(id);
        if (!deleted) return NotFound();

        return NoContent();
    }
}