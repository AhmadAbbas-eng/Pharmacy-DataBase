using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class EmployeeController : ControllerBase
{
    private readonly EmployeeMapper _employeeMapper;
    private readonly IEmployeeService _employeeService;

    public EmployeeController(IEmployeeService employeeService, EmployeeMapper employeeMapper)
    {
        _employeeService = employeeService;
        _employeeMapper = employeeMapper;
    }

    [HttpGet("start-date-range")]
    public async Task<IActionResult> GetByStartDateRange(DateTime startDate, DateTime endDate)
    {
        var employees = await _employeeService.GetEmployeesByStartDateRangeAsync(startDate, endDate);
        var employeeDtos = employees.Select(_employeeMapper.ToDto);
        return Ok(employeeDtos);
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
        var employeeDtos = employees.Select(_employeeMapper.ToDto);
        return Ok(employeeDtos);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var employee = await _employeeService.GetEmployeeByIdAsync(id);
        if (employee == null) return NotFound();

        var employeeDto = _employeeMapper.ToDto(employee);
        return Ok(employeeDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(EmployeeDto employeeDto)
    {
        var employeeDomain = _employeeMapper.ToDomain(employeeDto);
        var addedEmployee = await _employeeService.AddEmployeeAsync(employeeDomain);
        var addedEmployeeDto = _employeeMapper.ToDto(addedEmployee);
        return CreatedAtAction(nameof(GetById), new { id = addedEmployeeDto.Id }, addedEmployeeDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, EmployeeDto employeeDto)
    {
        if (id != employeeDto.Id) return BadRequest();

        var employeeDomain = _employeeMapper.ToDomain(employeeDto);
        var updatedEmployee = await _employeeService.UpdateEmployeeAsync(employeeDomain);
        if (updatedEmployee == null) return NotFound();
        var updatedEmployeeDto = _employeeMapper.ToDto(updatedEmployee);
        return Ok(updatedEmployeeDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _employeeService.DeleteEmployeeAsync(id);
        if (!deleted) return NotFound();

        return NoContent();
    }
}