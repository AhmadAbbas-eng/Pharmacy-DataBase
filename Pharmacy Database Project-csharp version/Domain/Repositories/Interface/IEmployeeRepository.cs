using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IEmployeeRepository
{
    Task<IEnumerable<EmployeeDomain>> ListEmployeesByStartDateAsync(DateTime startDate, DateTime endDate);
    Task DeleteEmployeePhoneAsync(int employeeId, string phoneNumber);
}