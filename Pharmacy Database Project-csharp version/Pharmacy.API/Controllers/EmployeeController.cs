using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class EmployeeController : ControllerBase
{
    private readonly IEmployeeService _employeeService;

    public EmployeeController(IEmployeeService employeeService)
    {
        _employeeService = employeeService;
    }

    [HttpGet("start-date-range")]
    public async Task<IActionResult> GetByStartDateRange(DateTime startDate, DateTime endDate)
    {
        var employees = await _employeeService.GetEmployeesByStartDateRangeAsync(startDate, endDate);
        return Ok(employees);
    }

    [HttpDelete("phone/{employeeId}")]
    public async Task<IActionResult> DeletePhone(int employeeId, string phoneNumber)
    {
        await _employeeService.DeleteEmployeePhoneAsync(employeeId, phoneNumber);
        return NoContent();
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var employees = await _employeeService.GetAllEmployeesAsync();
        return Ok(employees);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var employee = await _employeeService.GetEmployeeByIdAsync(id);
        if (employee == null) return NotFound();
        return Ok(employee);
    }

    [HttpPost]
    public async Task<IActionResult> Add(EmployeeDomain employee)
    {
        var addedEmployee = await _employeeService.AddEmployeeAsync(employee);
        return CreatedAtAction(nameof(GetById), new { id = addedEmployee.Id }, addedEmployee);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, EmployeeDomain employee)
    {
        if (id != employee.Id) return BadRequest();

        var updatedEmployee = await _employeeService.UpdateEmployeeAsync(employee);
        if (updatedEmployee == null) return NotFound();
        return Ok(updatedEmployee);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _employeeService.DeleteEmployeeAsync(id);
        if (!deleted) return NotFound();
        return NoContent();
    }
}