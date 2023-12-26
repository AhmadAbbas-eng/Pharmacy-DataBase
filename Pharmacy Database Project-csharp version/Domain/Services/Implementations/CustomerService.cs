using Domain.Exceptions;
using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;
using Microsoft.Extensions.Logging;

namespace Domain.Services.Implementations;

public class CustomerService : ICustomerService
{
    private readonly ICustomerRepository _customerRepository;
    private readonly ILogger<CustomerService> _logger;
    
    public CustomerService(ICustomerRepository customerRepository, ILogger<CustomerService> logger)
    {
        _customerRepository = customerRepository;
        _logger = logger;
    }
    
    public async Task<IEnumerable<CustomerDomain>> GetAllAsync()
    {
        return await Task.Run(() => _customerRepository.GetAllAsync());
    }

    public async Task<CustomerDomain> GetById(int id)
    {

        var employee = await _customerRepository.GetByIdAsync(id);
        if (employee == null)
        {
            throw new NoUserFoundException($"No User with the ID {id}");
        }

        return employee;
    }

    public async Task<int> AddAsync(CustomerDomain employeeDomain)
    {
        return await _customerRepository.AddAsync(employeeDomain);
    }

    public async Task DeleteAsync(int id)
    {
        await _customerRepository.DeleteAsync(id);
    }

    public async Task UpdateAsync(int id, CustomerDomain employeeDomain)
    {
        employeeDomain.CustomerId = id;
        await _customerRepository.UpdateAsync(employeeDomain);
    }

    public async Task<IEnumerable<CustomerDomain>> SearchAsync(string keyword)
    {
        if (string.IsNullOrWhiteSpace(keyword))
        {
            return new List<CustomerDomain>();
        }

        return await _customerRepository.FindAsync(e => e.Name.Contains(keyword) || e.CustomerNID.Contains(keyword));
    }
}