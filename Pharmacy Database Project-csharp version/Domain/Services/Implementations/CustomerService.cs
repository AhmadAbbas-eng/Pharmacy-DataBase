using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class CustomerService : ICustomerService
{
    private readonly ICustomerRepository _customerRepository;

    public CustomerService(ICustomerRepository customerRepository)
    {
        _customerRepository = customerRepository;
    }

    public async Task<double> CalculateTotalDebtAsync(string customerId)
    {
        return await _customerRepository.CalculateTotalDebtByIdAsync(customerId);
    }

    public async Task<IEnumerable<CustomerDomain>> GetCustomersWithOrdersAsync()
    {
        return await _customerRepository.FindWithOrdersAsync();
    }

    public async Task UpdateCustomerPhoneAsync(string oldPhoneNumber, string newPhoneNumber)
    {
        await _customerRepository.UpdatePhoneAsync(oldPhoneNumber, newPhoneNumber);
    }

    public async Task<IEnumerable<CustomerDomain>> GetAllCustomersAsync()
    {
        return await _customerRepository.GetAllAsync();
    }

    public async Task<CustomerDomain?> GetCustomerByIdAsync(string customerId)
    {
        return await _customerRepository.GetByIdAsync(customerId);
    }

    public async Task<CustomerDomain> AddCustomerAsync(CustomerDomain customer)
    {
        return await _customerRepository.AddAsync(customer);
    }

    public async Task<CustomerDomain?> UpdateCustomerAsync(CustomerDomain customer)
    {
        return await _customerRepository.UpdateAsync(customer);
    }

    public async Task<bool> DeleteCustomerAsync(string customerId)
    {
        return await _customerRepository.DeleteAsync(customerId);
    }
}