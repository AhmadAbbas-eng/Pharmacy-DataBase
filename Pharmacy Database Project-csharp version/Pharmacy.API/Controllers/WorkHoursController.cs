using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class WorkHoursController : ControllerBase
{
    private readonly IWorkHoursService _workHoursService;

    public WorkHoursController(IWorkHoursService workHoursService)
    {
        _workHoursService = workHoursService;
    }

    [HttpGet("employee/{employeeId}/month/{month}/year/{year}")]
    public async Task<IActionResult> GetWorkingHoursByEmployeeIdMonthAndYear(int employeeId, int month, int year)
    {
        var workHours = await _workHoursService.GetWorkingHoursByEmployeeIdMonthAndYearAsync(employeeId, month, year);
        return Ok(workHours);
    }

    [HttpGet("month/{month}/year/{year}")]
    public async Task<IActionResult> GetWorkingHoursByMonthAndYear(int month, int year)
    {
        var workHours = await _workHoursService.GetWorkingHoursByMonthAndYearAsync(month, year);
        return Ok(workHours);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var workHours = await _workHoursService.GetAllWorkHoursAsync();
        return Ok(workHours);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var workHours = await _workHoursService.GetWorkHoursByIdAsync(id);
        if (workHours == null) return NotFound();
        return Ok(workHours);
    }

    [HttpPost]
    public async Task<IActionResult> Add(WorkHoursDomain workHours)
    {
        var addedWorkHours = await _workHoursService.AddWorkHoursAsync(workHours);
        return CreatedAtAction(nameof(GetById), new { id = addedWorkHours.Id }, addedWorkHours);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, WorkHoursDomain workHours)
    {
        if (id != workHours.Id) return BadRequest();

        var updatedWorkHours = await _workHoursService.UpdateWorkHoursAsync(workHours);
        if (updatedWorkHours == null) return NotFound();
        return Ok(updatedWorkHours);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _workHoursService.DeleteWorkHoursAsync(id);
        if (!deleted) return NotFound();
        return NoContent();
    }
}