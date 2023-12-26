using AutoMapper;
using Controllers.Mappers;
using Controllers.Model.Dto;
using Domain.Exceptions;
using Domain.Models;
using Domain.Services.Interfaces;
using Infrastructure.Entities;
using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;

namespace Controllers.Controllers;

[ApiController]
[Route("/api/employee")]
public class EmployeeController : ControllerBase
{
    private readonly IEmployeeService _employeeService;
    private readonly ILogger<EmployeeController> _logger;
    private readonly EmployeeMapper _employeeMapper;
    public EmployeeController(IEmployeeService employeeService, ILogger<EmployeeController> logger, EmployeeMapper employeeMapper)
    {
        _employeeService = employeeService;
        _logger = logger;
        _employeeMapper = employeeMapper;
    }
    
    [HttpGet]
    public async Task<IActionResult> GetAllEmployees()
    {
        try
        {
            var employees = await _employeeService.GetAllAsync();
            var employeeDto = _employeeMapper.EmployeeToDto(employees);
            return Ok(employeeDto);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error occurred in GetAllEmployees");
            return StatusCode(500, "Internal server error");
        }
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetEmployee(int id)
    {
        try
        {
            var employee = await _employeeService.GetEmployeeById(id);
            var employeeDto = _employeeMapper.EmployeeToDto(employee);
            return Ok(employeeDto);
        }
        catch (NoUserFoundException ex)
        {
            _logger.LogError(ex.Message);
            return NotFound();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error occurred in GetEmployee");
            return StatusCode(500, "Internal server error");
        }
    }
    
    [HttpPost]
    public async Task<IActionResult> CreateEmployee([FromBody] EmployeeDto employee)
    {
        try
        {
            var employeeDomain = _employeeMapper.EmployeeToDomain(employee);
            employeeDomain.DateOfWork = DateTime.Today;
            var createdEmployeeId = await _employeeService.AddAsync(employeeDomain);
            
            return CreatedAtAction(nameof(GetEmployee), new { id = createdEmployeeId}, employee);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error occurred while creating new employee.");
            return StatusCode(500, "Internal server error");
        }
    }
    
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteEmployee(int id)
    {
        try
        {
            await _employeeService.DeleteAsync(id);
            return Ok($"Employee with ID {id} has been deleted.");
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error occurred in DeleteEmployee for ID {id}");
            return StatusCode(500, "Internal server error");
        }
    }
    
    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateEmployee(int id, [FromBody] EmployeeDto employee)
    {
        try
        {
            var employeeToUpdate =  _employeeMapper.EmployeeToDomain(employee);
            await _employeeService.UpdateAsync(id, employeeToUpdate);
            return Ok(employeeToUpdate);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error occurred while updating employee with ID {id}.");
            return StatusCode(500, "Internal server error");
        }
    }

    [HttpGet("search")]
    public async Task<IActionResult> SearchEmployees(string keyword)
    {
        try
        {
            var employees = await _employeeService.SearchAsync(keyword);
            var employeesDto =  _employeeMapper.EmployeeToDto(employees);
            return Ok(employeesDto);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error occurred in SearchEmployees");
            return StatusCode(500, "Internal server error");
        }
    }
    
    [HttpPatch("{id}")]
    public async Task<IActionResult> PatchEmployee(int id, [FromBody] JsonPatchDocument<EmployeeDto>? patchDoc)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }
        
        if (patchDoc is null)
        {
            return BadRequest();
        }

        var employeeDomain = await _employeeService.GetEmployeeById(id);

        if (employeeDomain is null)
        {
            return NotFound();
        }

        var employeeDto = _employeeMapper.EmployeeToDto(employeeDomain); 

        patchDoc.ApplyTo(employeeDto, error =>
        {
            ModelState.AddModelError(error.Operation.path, error.ErrorMessage);
        });
        

        employeeDomain = _employeeMapper.EmployeeToDomain(employeeDto);

        await _employeeService.UpdateAsync(employeeDomain.EmployeeId, employeeDomain);

        return Ok(employeeDomain);
    }

}
