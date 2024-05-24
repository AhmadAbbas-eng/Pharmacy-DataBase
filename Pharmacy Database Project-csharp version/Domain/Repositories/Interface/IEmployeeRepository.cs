using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IEmployeeRepository
{
    Task<IEnumerable<EmployeeDomain>> ListByStartDateAsync(DateTime startDate, DateTime endDate);
    Task DeletePhoneAsync(int employeeId, string phoneNumber);
}