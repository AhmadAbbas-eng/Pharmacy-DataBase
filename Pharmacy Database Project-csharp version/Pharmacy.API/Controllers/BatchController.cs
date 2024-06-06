using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class BatchController : ControllerBase
{
    private readonly IBatchService _batchService;

    public BatchController(IBatchService batchService)
    {
        _batchService = batchService;
    }

    [HttpGet("calculate-lifetime")]
    public async Task<IActionResult> CalculateLifetime()
    {
        var result = await _batchService.CalculateBatchLifetimeAsync();
        return Ok(result);
    }

    [HttpGet("production-date-range")]
    public async Task<IActionResult> GetByProductionDateRange(DateTime startDate, DateTime endDate)
    {
        var result = await _batchService.GetBatchesByProductionDateRangeAsync(startDate, endDate);
        return Ok(result);
    }

    [HttpGet("expiry-date-range")]
    public async Task<IActionResult> GetByExpiryDateRange(DateTime startDate, DateTime endDate)
    {
        var result = await _batchService.GetBatchesByExpiryDateRangeAsync(startDate, endDate);
        return Ok(result);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var result = await _batchService.GetAllBatchesAsync();
        return Ok(result);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var result = await _batchService.GetBatchByIdAsync(id);
        if (result == null) return NotFound();
        return Ok(result);
    }

    [HttpPost]
    public async Task<IActionResult> Add(BatchDomain batch)
    {
        var result = await _batchService.AddBatchAsync(batch);
        return CreatedAtAction(nameof(GetById), new { id = result.BatchId }, result);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, BatchDomain batch)
    {
        if (id != batch.BatchId) return BadRequest();

        var result = await _batchService.UpdateBatchAsync(batch);
        if (result == null) return NotFound();
        return Ok(result);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var result = await _batchService.DeleteBatchAsync(id);
        if (!result) return NotFound();
        return NoContent();
    }
}