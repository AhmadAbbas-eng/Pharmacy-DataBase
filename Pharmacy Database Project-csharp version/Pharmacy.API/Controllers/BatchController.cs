using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class BatchController : ControllerBase
{
    private readonly BatchLifetimeMapper _batchLifetimeMapper;
    private readonly BatchMapper _batchMapper;
    private readonly IBatchService _batchService;

    public BatchController(IBatchService batchService, BatchMapper batchMapper, BatchLifetimeMapper batchLifetimeMapper)
    {
        _batchService = batchService;
        _batchMapper = batchMapper;
        _batchLifetimeMapper = batchLifetimeMapper;
    }

    [HttpGet("calculate-lifetime")]
    public async Task<IActionResult> CalculateLifetime()
    {
        var result = await _batchService.CalculateBatchLifetimeAsync();
        var resultDtos = result.Select(_batchLifetimeMapper.ToDto);
        return Ok(resultDtos);
    }

    [HttpGet("production-date-range")]
    public async Task<IActionResult> GetByProductionDateRange(DateTime startDate, DateTime endDate)
    {
        var result = await _batchService.GetBatchesByProductionDateRangeAsync(startDate, endDate);
        var resultDtos = result.Select(_batchMapper.ToDto);
        return Ok(resultDtos);
    }

    [HttpGet("expiry-date-range")]
    public async Task<IActionResult> GetByExpiryDateRange(DateTime startDate, DateTime endDate)
    {
        var result = await _batchService.GetBatchesByExpiryDateRangeAsync(startDate, endDate);
        var resultDtos = result.Select(_batchMapper.ToDto);
        return Ok(resultDtos);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var result = await _batchService.GetAllBatchesAsync();
        var resultDtos = result.Select(_batchMapper.ToDto);
        return Ok(resultDtos);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var result = await _batchService.GetBatchByIdAsync(id);
        if (result == null) return NotFound();

        var resultDto = _batchMapper.ToDto(result);
        return Ok(resultDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(BatchDto batchDto)
    {
        var batchDomain = _batchMapper.ToDomain(batchDto);
        var result = await _batchService.AddBatchAsync(batchDomain);
        var resultDto = _batchMapper.ToDto(result);
        return CreatedAtAction(nameof(GetById), new { id = resultDto.BatchId }, resultDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, BatchDto batchDto)
    {
        if (id != batchDto.BatchId) return BadRequest();

        var batchDomain = _batchMapper.ToDomain(batchDto);
        var result = await _batchService.UpdateBatchAsync(batchDomain);
        if (result == null) return NotFound();

        var resultDto = _batchMapper.ToDto(result);
        return Ok(resultDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var result = await _batchService.DeleteBatchAsync(id);
        if (!result) return NotFound();

        return NoContent();
    }
}