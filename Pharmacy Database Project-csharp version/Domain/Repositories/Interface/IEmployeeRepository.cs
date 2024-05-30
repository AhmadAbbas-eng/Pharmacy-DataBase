using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IEmployeeRepository
{
    Task<IEnumerable<EmployeeDomain>> ListByStartDateAsync(DateTime startDate, DateTime endDate);
    Task DeletePhoneAsync(int employeeId, string phoneNumber);
    Task<IEnumerable<EmployeeDomain>> GetAllAsync();
    Task<EmployeeDomain?> GetByIdAsync(int id);
    Task<EmployeeDomain> AddAsync(EmployeeDomain employee);
    Task<EmployeeDomain?> UpdateAsync(EmployeeDomain employee);
    Task<bool> DeleteAsync(int id);
}