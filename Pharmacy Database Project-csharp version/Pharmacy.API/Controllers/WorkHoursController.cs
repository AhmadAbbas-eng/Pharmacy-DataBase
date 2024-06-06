using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class WorkHoursController : ControllerBase
{
    private readonly WorkHoursMapper _workHoursMapper;
    private readonly IWorkHoursService _workHoursService;

    public WorkHoursController(IWorkHoursService workHoursService, WorkHoursMapper workHoursMapper)
    {
        _workHoursService = workHoursService;
        _workHoursMapper = workHoursMapper;
    }

    [HttpGet("employee/{employeeId}/month/{month}/year/{year}")]
    public async Task<IActionResult> GetWorkingHoursByEmployeeIdMonthAndYear(int employeeId, int month, int year)
    {
        var workHours = await _workHoursService.GetWorkingHoursByEmployeeIdMonthAndYearAsync(employeeId, month, year);
        var workHoursDto = workHours.Select(_workHoursMapper.ToDto);
        return Ok(workHoursDto);
    }

    [HttpGet("month/{month}/year/{year}")]
    public async Task<IActionResult> GetWorkingHoursByMonthAndYear(int month, int year)
    {
        var workHours = await _workHoursService.GetWorkingHoursByMonthAndYearAsync(month, year);
        var workHoursDto = workHours.Select(_workHoursMapper.ToDto);
        return Ok(workHoursDto);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var workHours = await _workHoursService.GetAllWorkHoursAsync();
        var workHoursDto = workHours.Select(_workHoursMapper.ToDto);
        return Ok(workHoursDto);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var workHours = await _workHoursService.GetWorkHoursByIdAsync(id);
        if (workHours == null) return NotFound();

        var workHoursDto = _workHoursMapper.ToDto(workHours);
        return Ok(workHoursDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(WorkHoursDto workHoursDto)
    {
        var workHoursDomain = _workHoursMapper.ToDomain(workHoursDto);
        var addedWorkHours = await _workHoursService.AddWorkHoursAsync(workHoursDomain);
        var addedWorkHoursDto = _workHoursMapper.ToDto(addedWorkHours);
        return CreatedAtAction(nameof(GetById), new { id = addedWorkHoursDto.Id }, addedWorkHoursDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, WorkHoursDto workHoursDto)
    {
        if (id != workHoursDto.Id) return BadRequest();

        var workHoursDomain = _workHoursMapper.ToDomain(workHoursDto);
        var updatedWorkHours = await _workHoursService.UpdateWorkHoursAsync(workHoursDomain);
        if (updatedWorkHours == null) return NotFound();

        var updatedWorkHoursDto = _workHoursMapper.ToDto(updatedWorkHours);
        return Ok(updatedWorkHoursDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _workHoursService.DeleteWorkHoursAsync(id);
        if (!deleted) return NotFound();

        return NoContent();
    }
}