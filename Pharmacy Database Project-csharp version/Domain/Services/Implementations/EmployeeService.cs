using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class EmployeeService : IEmployeeService
{
    private readonly IEmployeeRepository _employeeRepository;

    public EmployeeService(IEmployeeRepository employeeRepository)
    {
        _employeeRepository = employeeRepository;
    }

    public async Task<IEnumerable<EmployeeDomain>> GetEmployeesByStartDateRangeAsync(DateTime startDate,
        DateTime endDate)
    {
        return await _employeeRepository.ListByStartDateAsync(startDate, endDate);
    }

    public async Task DeleteEmployeePhoneAsync(int employeeId, string phoneNumber)
    {
        await _employeeRepository.DeletePhoneAsync(employeeId, phoneNumber);
    }

    public async Task<IEnumerable<EmployeeDomain>> GetAllEmployeesAsync()
    {
        return await _employeeRepository.GetAllAsync();
    }

    public async Task<EmployeeDomain?> GetEmployeeByIdAsync(int id)
    {
        return await _employeeRepository.GetByIdAsync(id);
    }

    public async Task<EmployeeDomain> AddEmployeeAsync(EmployeeDomain employee)
    {
        return await _employeeRepository.AddAsync(employee);
    }

    public async Task<EmployeeDomain?> UpdateEmployeeAsync(EmployeeDomain employee)
    {
        return await _employeeRepository.UpdateAsync(employee);
    }

    public async Task<bool> DeleteEmployeeAsync(int id)
    {
        return await _employeeRepository.DeleteAsync(id);
    }
}