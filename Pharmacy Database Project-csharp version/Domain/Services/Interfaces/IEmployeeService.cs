using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IEmployeeService
{
    Task<IEnumerable<EmployeeDomain>> GetAllAsync();
    Task<EmployeeDomain>? GetEmployeeById(int id);
    Task<int> AddAsync(EmployeeDomain employeeDomain);
    Task DeleteAsync(int id);
    Task UpdateAsync(int id, EmployeeDomain employeeDomain);
    Task<IEnumerable<EmployeeDomain>> SearchAsync(string keyword);
    EmployeeDomain ValidatePassword(string userName, string password);
}