using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class SupplierController : ControllerBase
{
    private readonly SupplierMapper _supplierMapper;
    private readonly ISupplierService _supplierService;

    public SupplierController(ISupplierService supplierService, SupplierMapper supplierMapper)
    {
        _supplierService = supplierService;
        _supplierMapper = supplierMapper;
    }

    [HttpGet("due-amounts")]
    public async Task<IActionResult> ListSuppliersWithDueAmounts()
    {
        var suppliers = await _supplierService.ListSuppliersWithDueAmountsAsync();
        var supplierDtos = suppliers.Select(_supplierMapper.ToDto);
        return Ok(supplierDtos);
    }

    [HttpGet("by-product/{productName}")]
    public async Task<IActionResult> FindByProduct(string productName)
    {
        var suppliers = await _supplierService.FindSuppliersByProductAsync(productName);
        var supplierDtos = suppliers.Select(_supplierMapper.ToDto);
        return Ok(supplierDtos);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var suppliers = await _supplierService.GetAllSuppliersAsync();
        var supplierDtos = suppliers.Select(_supplierMapper.ToDto);
        return Ok(supplierDtos);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var supplier = await _supplierService.GetSupplierByIdAsync(id);
        if (supplier == null) return NotFound();

        var supplierDto = _supplierMapper.ToDto(supplier);
        return Ok(supplierDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(SupplierDto supplierDto)
    {
        var supplierDomain = _supplierMapper.ToDomain(supplierDto);
        var addedSupplier = await _supplierService.AddSupplierAsync(supplierDomain);
        var addedSupplierDto = _supplierMapper.ToDto(addedSupplier);
        return CreatedAtAction(nameof(GetById), new { id = addedSupplierDto.Id }, addedSupplierDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, SupplierDto supplierDto)
    {
        if (id != supplierDto.Id) return BadRequest();

        var supplierDomain = _supplierMapper.ToDomain(supplierDto);
        var updatedSupplier = await _supplierService.UpdateSupplierAsync(supplierDomain);
        if (updatedSupplier == null) return NotFound();

        var updatedSupplierDto = _supplierMapper.ToDto(updatedSupplier);
        return Ok(updatedSupplierDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _supplierService.DeleteSupplierAsync(id);
        if (!deleted) return NotFound();

        return NoContent();
    }
}