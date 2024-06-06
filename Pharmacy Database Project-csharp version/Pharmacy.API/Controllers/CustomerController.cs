using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class CustomerController : ControllerBase
{
    private readonly ICustomerService _customerService;

    public CustomerController(ICustomerService customerService)
    {
        _customerService = customerService;
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
        return Ok(customers);
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
        return Ok(customers);
    }

    [HttpGet("{customerId}")]
    public async Task<IActionResult> GetById(string customerId)
    {
        var customer = await _customerService.GetCustomerByIdAsync(customerId);
        if (customer == null) return NotFound();
        return Ok(customer);
    }

    [HttpPost]
    public async Task<IActionResult> Add(CustomerDomain customer)
    {
        var addedCustomer = await _customerService.AddCustomerAsync(customer);
        return CreatedAtAction(nameof(GetById), new { customerId = addedCustomer.Id }, addedCustomer);
    }

    [HttpPut("{customerId}")]
    public async Task<IActionResult> Update(int customerId, CustomerDomain customer)
    {
        if (customerId != customer.Id) return BadRequest();

        var updatedCustomer = await _customerService.UpdateCustomerAsync(customer);
        if (updatedCustomer == null) return NotFound();
        return Ok(updatedCustomer);
    }

    [HttpDelete("{customerId}")]
    public async Task<IActionResult> Delete(string customerId)
    {
        var deleted = await _customerService.DeleteCustomerAsync(customerId);
        if (!deleted) return NotFound();
        return NoContent();
    }
}