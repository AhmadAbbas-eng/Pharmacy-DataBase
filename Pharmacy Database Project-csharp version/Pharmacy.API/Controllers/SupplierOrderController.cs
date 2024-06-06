using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class SupplierOrderController : ControllerBase
{
    private readonly SupplierOrderMapper _supplierOrderMapper;
    private readonly ISupplierOrderService _supplierOrderService;

    public SupplierOrderController(ISupplierOrderService supplierOrderService, SupplierOrderMapper supplierOrderMapper)
    {
        _supplierOrderService = supplierOrderService;
        _supplierOrderMapper = supplierOrderMapper;
    }

    [HttpGet("order/{orderId}")]
    public async Task<IActionResult> GetByOrderId(int orderId)
    {
        var supplierOrder = await _supplierOrderService.GetOrderByOrderIdAsync(orderId);
        var supplierOrderDto = _supplierOrderMapper.ToDto(supplierOrder);
        return Ok(supplierOrderDto);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var supplierOrders = await _supplierOrderService.GetAllSupplierOrdersAsync();
        var supplierOrderDtos = supplierOrders.Select(_supplierOrderMapper.ToDto);
        return Ok(supplierOrderDtos);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var supplierOrder = await _supplierOrderService.GetSupplierOrderByIdAsync(id);
        if (supplierOrder == null) return NotFound();

        var supplierOrderDto = _supplierOrderMapper.ToDto(supplierOrder);
        return Ok(supplierOrderDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(SupplierOrderDto supplierOrderDto)
    {
        var supplierOrderDomain = _supplierOrderMapper.ToDomain(supplierOrderDto);
        var addedSupplierOrder = await _supplierOrderService.AddSupplierOrderAsync(supplierOrderDomain);
        var addedSupplierOrderDto = _supplierOrderMapper.ToDto(addedSupplierOrder);
        return CreatedAtAction(nameof(GetById), new { id = addedSupplierOrderDto.Id }, addedSupplierOrderDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, SupplierOrderDto supplierOrderDto)
    {
        if (id != supplierOrderDto.Id) return BadRequest();

        var supplierOrderDomain = _supplierOrderMapper.ToDomain(supplierOrderDto);
        var updatedSupplierOrder = await _supplierOrderService.UpdateSupplierOrderAsync(supplierOrderDomain);
        if (updatedSupplierOrder == null) return NotFound();
        var updatedSupplierOrderDto = _supplierOrderMapper.ToDto(updatedSupplierOrder);
        return Ok(updatedSupplierOrderDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _supplierOrderService.DeleteSupplierOrderAsync(id);
        if (!deleted) return NotFound();

        return NoContent();
    }
}