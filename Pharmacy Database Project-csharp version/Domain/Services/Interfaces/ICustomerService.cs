using Domain.Models;

namespace Domain.Services.Interfaces;

public interface ICustomerService
{
    Task<IEnumerable<CustomerDomain>> GetAllAsync();
    Task<CustomerDomain>? GetById(int id);
    Task<int> AddAsync(CustomerDomain employeeDomain);
    Task DeleteAsync(int id);
    Task UpdateAsync(int id, CustomerDomain employeeDomain);
    Task<IEnumerable<CustomerDomain>> SearchAsync(string keyword);
}