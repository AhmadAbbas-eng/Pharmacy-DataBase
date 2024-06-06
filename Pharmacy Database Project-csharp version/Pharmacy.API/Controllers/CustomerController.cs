using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class CustomerController : ControllerBase
{
    private readonly CustomerMapper _customerMapper;
    private readonly ICustomerService _customerService;

    public CustomerController(ICustomerService customerService, CustomerMapper customerMapper)
    {
        _customerService = customerService;
        _customerMapper = customerMapper;
    }

    [HttpGet("total-debt/{customerId}")]
    public async Task<IActionResult> CalculateTotalDebt(string customerId)
    {
        var totalDebt = await _customerService.CalculateTotalDebtAsync(customerId);
        return Ok(totalDebt);
    }

    [HttpGet("with-orders")]
    public async Task<IActionResult> GetCustomersWithOrders()
    {
        var customers = await _customerService.GetCustomersWithOrdersAsync();
        var customerDtos = customers.Select(_customerMapper.ToDto);
        return Ok(customerDtos);
    }

    [HttpPut("update-phone")]
    public async Task<IActionResult> UpdateCustomerPhone(string oldPhoneNumber, string newPhoneNumber)
    {
        await _customerService.UpdateCustomerPhoneAsync(oldPhoneNumber, newPhoneNumber);
        return NoContent();
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var customers = await _customerService.GetAllCustomersAsync();
        var customerDtos = customers.Select(_customerMapper.ToDto);
        return Ok(customerDtos);
    }

    [HttpGet("{customerId}")]
    public async Task<IActionResult> GetById(string customerId)
    {
        var customer = await _customerService.GetCustomerByIdAsync(customerId);
        if (customer == null) return NotFound();

        var customerDto = _customerMapper.ToDto(customer);
        return Ok(customerDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(CustomerDto customerDto)
    {
        var customerDomain = _customerMapper.ToDomain(customerDto);
        var addedCustomer = await _customerService.AddCustomerAsync(customerDomain);
        var addedCustomerDto = _customerMapper.ToDto(addedCustomer);
        return CreatedAtAction(nameof(GetById), new { customerId = addedCustomerDto.Id }, addedCustomerDto);
    }

    [HttpPut("{customerId}")]
    public async Task<IActionResult> Update(int customerId, CustomerDto customerDto)
    {
        if (customerId != customerDto.Id) return BadRequest();

        var customerDomain = _customerMapper.ToDomain(customerDto);
        var updatedCustomer = await _customerService.UpdateCustomerAsync(customerDomain);
        if (updatedCustomer == null) return NotFound();

        var updatedCustomerDto = _customerMapper.ToDto(updatedCustomer);
        return Ok(updatedCustomerDto);
    }

    [HttpDelete("{customerId}")]
    public async Task<IActionResult> Delete(string customerId)
    {
        var deleted = await _customerService.DeleteCustomerAsync(customerId);
        if (!deleted) return NotFound();

        return NoContent();
    }
}