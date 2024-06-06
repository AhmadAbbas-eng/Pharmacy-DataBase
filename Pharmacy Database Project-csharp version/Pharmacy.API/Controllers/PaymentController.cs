using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class PaymentController : ControllerBase
{
    private readonly BatchMapper _batchMapper;
    private readonly PaymentMapper _paymentMapper;
    private readonly IPaymentService _paymentService;
    private readonly PendingPaymentMapper _pendingPaymentMapper;

    public PaymentController(IPaymentService paymentService, PaymentMapper paymentMapper, BatchMapper batchMapper,
        PendingPaymentMapper pendingPaymentMapper)
    {
        _paymentService = paymentService;
        _paymentMapper = paymentMapper;
        _batchMapper = batchMapper;
        _pendingPaymentMapper = pendingPaymentMapper;
    }

    [HttpGet("pending")]
    public async Task<IActionResult> GetPendingPayments()
    {
        var pendingPayments = await _paymentService.GetPendingPaymentsAsync();
        var pendingPaymentDtos = pendingPayments.Select(_pendingPaymentMapper.ToDto);
        return Ok(pendingPaymentDtos);
    }

    [HttpPost("add-batches")]
    public async Task<IActionResult> AddAllBatches(IEnumerable<BatchDto> batchDtos)
    {
        var batchDomains = batchDtos.Select(_batchMapper.ToDomain);
        await _paymentService.AddAllBatchesAsync(batchDomains);
        return NoContent();
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var payments = await _paymentService.GetAllPaymentsAsync();
        var paymentDtos = payments.Select(_paymentMapper.ToDto);
        return Ok(paymentDtos);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var payment = await _paymentService.GetPaymentByIdAsync(id);
        if (payment == null) return NotFound();

        var paymentDto = _paymentMapper.ToDto(payment);
        return Ok(paymentDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(PaymentDto paymentDto)
    {
        var paymentDomain = _paymentMapper.ToDomain(paymentDto);
        var addedPayment = await _paymentService.AddPaymentAsync(paymentDomain);
        var addedPaymentDto = _paymentMapper.ToDto(addedPayment);
        return CreatedAtAction(nameof(GetById), new { id = addedPaymentDto.Id }, addedPaymentDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, PaymentDto paymentDto)
    {
        if (id != paymentDto.Id) return BadRequest();

        var paymentDomain = _paymentMapper.ToDomain(paymentDto);
        var updatedPayment = await _paymentService.UpdatePaymentAsync(paymentDomain);
        if (updatedPayment == null) return NotFound();

        var updatedPaymentDto = _paymentMapper.ToDto(updatedPayment);
        return Ok(updatedPaymentDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _paymentService.DeletePaymentAsync(id);
        if (!deleted) return NotFound();

        return NoContent();
    }
}