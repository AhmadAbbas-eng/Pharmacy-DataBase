using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class SupplierController : ControllerBase
{
    private readonly ISupplierService _supplierService;

    public SupplierController(ISupplierService supplierService)
    {
        _supplierService = supplierService;
    }

    [HttpGet("due-amounts")]
    public async Task<IActionResult> ListSuppliersWithDueAmounts()
    {
        var suppliers = await _supplierService.ListSuppliersWithDueAmountsAsync();
        return Ok(suppliers);
    }

    [HttpGet("by-product/{productName}")]
    public async Task<IActionResult> FindByProduct(string productName)
    {
        var suppliers = await _supplierService.FindSuppliersByProductAsync(productName);
        return Ok(suppliers);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var suppliers = await _supplierService.GetAllSuppliersAsync();
        return Ok(suppliers);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var supplier = await _supplierService.GetSupplierByIdAsync(id);
        if (supplier == null) return NotFound();
        return Ok(supplier);
    }

    [HttpPost]
    public async Task<IActionResult> Add(SupplierDomain supplier)
    {
        var addedSupplier = await _supplierService.AddSupplierAsync(supplier);
        return CreatedAtAction(nameof(GetById), new { id = addedSupplier.Id }, addedSupplier);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, SupplierDomain supplier)
    {
        if (id != supplier.Id) return BadRequest();

        var updatedSupplier = await _supplierService.UpdateSupplierAsync(supplier);
        if (updatedSupplier == null) return NotFound();
        return Ok(updatedSupplier);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _supplierService.DeleteSupplierAsync(id);
        if (!deleted) return NotFound();
        return NoContent();
    }
}