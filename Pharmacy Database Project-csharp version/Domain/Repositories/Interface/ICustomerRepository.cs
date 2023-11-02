using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ICustomerRepository
{
    Task<double> CalculateTotalDebtByCustomerAsync(string customerId);
    Task<IEnumerable<CustomerDomain>> FindCustomersWithOrdersAsync();
    Task UpdateCustomerPhoneAsync(string oldPhoneNumber, string newPhoneNumber);
}