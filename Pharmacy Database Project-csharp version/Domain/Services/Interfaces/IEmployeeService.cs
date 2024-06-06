using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IEmployeeService
{
    Task<IEnumerable<EmployeeDomain>> GetEmployeesByStartDateRangeAsync(DateTime startDate, DateTime endDate);
    Task DeleteEmployeePhoneAsync(int employeeId, string phoneNumber);
    Task<IEnumerable<EmployeeDomain>> GetAllEmployeesAsync();
    Task<EmployeeDomain?> GetEmployeeByIdAsync(int id);
    Task<EmployeeDomain> AddEmployeeAsync(EmployeeDomain employee);
    Task<EmployeeDomain?> UpdateEmployeeAsync(EmployeeDomain employee);
    Task<bool> DeleteEmployeeAsync(int id);
}