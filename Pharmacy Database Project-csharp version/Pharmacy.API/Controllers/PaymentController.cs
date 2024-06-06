using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class PaymentController : ControllerBase
{
    private readonly IPaymentService _paymentService;

    public PaymentController(IPaymentService paymentService)
    {
        _paymentService = paymentService;
    }

    [HttpGet("pending")]
    public async Task<IActionResult> GetPendingPayments()
    {
        var pendingPayments = await _paymentService.GetPendingPaymentsAsync();
        return Ok(pendingPayments);
    }

    [HttpPost("add-batches")]
    public async Task<IActionResult> AddAllBatches(IEnumerable<BatchDomain> batches)
    {
        await _paymentService.AddAllBatchesAsync(batches);
        return NoContent();
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var payments = await _paymentService.GetAllPaymentsAsync();
        return Ok(payments);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var payment = await _paymentService.GetPaymentByIdAsync(id);
        if (payment == null) return NotFound();
        return Ok(payment);
    }

    [HttpPost]
    public async Task<IActionResult> Add(PaymentDomain payment)
    {
        var addedPayment = await _paymentService.AddPaymentAsync(payment);
        return CreatedAtAction(nameof(GetById), new { id = addedPayment.Id }, addedPayment);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, PaymentDomain payment)
    {
        if (id != payment.Id) return BadRequest();

        var updatedPayment = await _paymentService.UpdatePaymentAsync(payment);
        if (updatedPayment == null) return NotFound();
        return Ok(updatedPayment);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _paymentService.DeletePaymentAsync(id);
        if (!deleted) return NotFound();
        return NoContent();
    }
}