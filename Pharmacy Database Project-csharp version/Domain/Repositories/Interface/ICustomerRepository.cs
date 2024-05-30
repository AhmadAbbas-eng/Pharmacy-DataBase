using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ICustomerRepository
{
    Task<double> CalculateTotalDebtByIdAsync(string customerId);
    Task<IEnumerable<CustomerDomain>> FindWithOrdersAsync();
    Task UpdatePhoneAsync(string oldPhoneNumber, string newPhoneNumber);
    Task<IEnumerable<CustomerDomain>> GetAllAsync();
    Task<CustomerDomain?> GetByIdAsync(string customerId);
    Task<CustomerDomain> AddAsync(CustomerDomain customer);
    Task<CustomerDomain?> UpdateAsync(CustomerDomain customer);
    Task<bool> DeleteAsync(string customerId);
}