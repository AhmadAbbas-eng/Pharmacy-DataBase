using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IEmployeeService
{
    Task<IEnumerable<EmployeeDomain>> GetAllAsync();
    Task<EmployeeDomain>? GetEmployeeById(int id);
    Task<int> AddAsync(EmployeeDomain employeeDomain);
    EmployeeDomain ValidatePassword(string userName, string password);
}