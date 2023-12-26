using Controllers.Mappers;
using Controllers.Model.Dto;


using Domain.Exceptions;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;

namespace Controllers.Controllers;

[ApiController]
[Route("/api/customer")]
public class CustomerController : ControllerBase
{
    private readonly ICustomerService _customerService;
    private readonly ILogger<CustomerController> _logger;
    private readonly CustomerMapper _customerMapper;

    public CustomerController(ICustomerService customerService, ILogger<CustomerController> logger, CustomerMapper customerMapper)
    {
        _customerService = customerService;
        _logger = logger;
        _customerMapper = customerMapper;
    }

    [HttpGet]
    public async Task<IActionResult> GetAllCustomers()
    {
        try
        {
            var customers = await _customerService.GetAllAsync();
            var customerDtos = _customerMapper.CustomerToDto(customers);
            return Ok(customerDtos);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error occurred in GetAllCustomers");
            return StatusCode(500, "Internal server error");
        }
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetCustomer(int id)
    {
        try
        {
            var customer = await _customerService.GetById(id);
            var customerDto = _customerMapper.CustomerToDto(customer);
            return Ok(customerDto);
        }
        catch (NoUserFoundException ex)
        {
            _logger.LogError(ex.Message);
            return NotFound();
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error occurred in GetCustomer");
            return StatusCode(500, "Internal server error");
        }
    }

    [HttpPost]
    public async Task<IActionResult> CreateCustomer([FromBody] CustomerDto customerDto)
    {
        try
        {
            var customerDomain = _customerMapper.CustomerToDomain(customerDto);
            var createdCustomerId = await _customerService.AddAsync(customerDomain);
            return CreatedAtAction(nameof(GetCustomer), new { id = createdCustomerId }, customerDto);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error occurred while creating new customer.");
            return StatusCode(500, "Internal server error");
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteCustomer(int id)
    {
        try
        {
            await _customerService.DeleteAsync(id);
            return Ok($"Customer with ID {id} has been deleted.");
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error occurred in DeleteCustomer for ID {id}");
            return StatusCode(500, "Internal server error");
        }
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateCustomer(int id, [FromBody] CustomerDto customerDto)
    {
        try
        {
            var customerToUpdate = _customerMapper.CustomerToDomain(customerDto);
            await _customerService.UpdateAsync(id, customerToUpdate);
            return Ok(customerToUpdate);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, $"Error occurred while updating customer with ID {id}.");
            return StatusCode(500, "Internal server error");
        }
    }

    [HttpGet("search")]
    public async Task<IActionResult> SearchCustomers(string keyword)
    {
        try
        {
            var customers = await _customerService.SearchAsync(keyword);
            var customerDtos = _customerMapper.CustomerToDto(customers);
            return Ok(customerDtos);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error occurred in SearchCustomers");
            return StatusCode(500, "Internal server error");
        }
    }

    [HttpPatch("{id}")]
    public async Task<IActionResult> PatchCustomer(int id, [FromBody] JsonPatchDocument<CustomerDto>? patchDoc)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }

        if (patchDoc is null)
        {
            return BadRequest();
        }

        var customerDomain = await _customerService.GetById(id);

        if (customerDomain is null)
        {
            return NotFound();
        }

        var customerDto = _customerMapper.CustomerToDto(customerDomain);

        patchDoc.ApplyTo(customerDto, error =>
        {
            ModelState.AddModelError(error.Operation.path, error.ErrorMessage);
        });

        customerDomain = _customerMapper.CustomerToDomain(customerDto);

        await _customerService.UpdateAsync(customerDomain.CustomerId, customerDomain);

        return Ok(customerDomain);
    }
}
