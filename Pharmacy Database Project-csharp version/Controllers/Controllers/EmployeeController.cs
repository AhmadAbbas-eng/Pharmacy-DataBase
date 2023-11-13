using AutoMapper;
using Controllers.Model.Dto;
using Domain.Models;
using Domain.Services.Interfaces;
using Infrastructure.Entities;
using Microsoft.AspNetCore.Mvc;

namespace Controllers.Controllers;

[ApiController]
[Route("/api/employee")]
public class EmployeeController :ControllerBase
{
    private readonly IEmployeeService _employeeService;
    private readonly ILogger<EmployeeController> _logger;
    private readonly IMapper _mapper;

    public EmployeeController(IEmployeeService employeeService, ILogger<EmployeeController> logger, IMapper mapper)
    {
        _employeeService = employeeService;
        _logger = logger;
        _mapper = mapper;
    }
    
    [HttpGet]
    public async Task<IActionResult> GetAllEmployees()
    {
        try
        {
            var employees = await _employeeService.GetAllAsync();
            return Ok(employees);
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
            return Ok(employee);
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
            var employeeDomain = _mapper.Map<EmployeeDomain>(employee);
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


}