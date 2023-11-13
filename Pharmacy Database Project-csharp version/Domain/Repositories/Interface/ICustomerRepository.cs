using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ICustomerRepository : IRepository<CustomerDomain, int>
{
    Task<double> CalculateTotalDebtByIdAsync(string id);
    Task<IEnumerable<CustomerDomain>> FindCustomersWithOrdersAsync();
    Task UpdateCustomerPhoneAsync(string oldPhoneNumber, string newPhoneNumber);
}