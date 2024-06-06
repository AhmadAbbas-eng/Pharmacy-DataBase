using Domain.Models;

namespace Domain.Services.Interfaces;

public interface ICustomerService
{
    Task<double> CalculateTotalDebtAsync(string customerId);
    Task<IEnumerable<CustomerDomain>> GetCustomersWithOrdersAsync();
    Task UpdateCustomerPhoneAsync(string oldPhoneNumber, string newPhoneNumber);
    Task<IEnumerable<CustomerDomain>> GetAllCustomersAsync();
    Task<CustomerDomain?> GetCustomerByIdAsync(string customerId);
    Task<CustomerDomain> AddCustomerAsync(CustomerDomain customer);
    Task<CustomerDomain?> UpdateCustomerAsync(CustomerDomain customer);
    Task<bool> DeleteCustomerAsync(string customerId);
}