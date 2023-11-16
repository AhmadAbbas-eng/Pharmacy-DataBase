using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ICustomerRepository : IRepository<CustomerDomain, int>
{
    Task<double> CalculateTotalDebtByIdAsync(string id);
    Task<IEnumerable<CustomerDomain>> FindWithOrdersAsync();
    Task UpdatePhoneAsync(string oldPhoneNumber, string newPhoneNumber);
}