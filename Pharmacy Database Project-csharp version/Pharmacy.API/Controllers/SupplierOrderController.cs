using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class SupplierOrderController : ControllerBase
{
    private readonly ISupplierOrderService _supplierOrderService;

    public SupplierOrderController(ISupplierOrderService supplierOrderService)
    {
        _supplierOrderService = supplierOrderService;
    }

    [HttpGet("order/{orderId}")]
    public async Task<IActionResult> GetByOrderId(int orderId)
    {
        var supplierOrder = await _supplierOrderService.GetOrderByOrderIdAsync(orderId);
        if (supplierOrder == null) return NotFound();
        return Ok(supplierOrder);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var supplierOrders = await _supplierOrderService.GetAllSupplierOrdersAsync();
        return Ok(supplierOrders);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var supplierOrder = await _supplierOrderService.GetSupplierOrderByIdAsync(id);
        if (supplierOrder == null) return NotFound();
        return Ok(supplierOrder);
    }

    [HttpPost]
    public async Task<IActionResult> Add(SupplierOrderDomain supplierOrder)
    {
        var addedSupplierOrder = await _supplierOrderService.AddSupplierOrderAsync(supplierOrder);
        return CreatedAtAction(nameof(GetById), new { id = addedSupplierOrder.Id }, addedSupplierOrder);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, SupplierOrderDomain supplierOrder)
    {
        if (id != supplierOrder.Id) return BadRequest();

        var updatedSupplierOrder = await _supplierOrderService.UpdateSupplierOrderAsync(supplierOrder);
        if (updatedSupplierOrder == null) return NotFound();
        return Ok(updatedSupplierOrder);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _supplierOrderService.DeleteSupplierOrderAsync(id);
        if (!deleted) return NotFound();
        return NoContent();
    }
}