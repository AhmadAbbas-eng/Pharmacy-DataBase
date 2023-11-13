using System.Security.Authentication;
using Domain.Exceptions;
using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;
using Microsoft.Extensions.Logging;

namespace Domain.Services.Implementations;

public class EmployeeService : IEmployeeService
{
    private readonly IRepository<EmployeeDomain, int> _employeeRepository;
    private readonly ILogger<EmployeeService> _logger;

    public EmployeeService(IRepository<EmployeeDomain, int> employeeRepository, ILogger<EmployeeService> logger)
    {
        _employeeRepository = employeeRepository;
        _logger = logger;
    }

    public async Task<IEnumerable<EmployeeDomain>> GetAllAsync()
    {
        return await Task.Run(() => _employeeRepository.GetAll());
    }

    public async Task<EmployeeDomain> GetEmployeeById(int id)
    {

        var employee = await _employeeRepository.GetByIdAsync(id);
        if (employee == null)
        {
            throw new NoUserFoundException($"No User with the ID {id}");
        }

        return employee;
    }

    public async Task<int> AddAsync(EmployeeDomain employeeDomain)
    {
        return await _employeeRepository.AddAsync(employeeDomain);
    }

    public EmployeeDomain ValidatePassword(string userName, string password)
    {
        var users = _employeeRepository.Find(e => e.Name.Equals(userName)).ToList();

        if (!users.Any())
        {
            _logger.LogError($"No User with the name {userName}");
            throw new NoUserFoundException($"No User with the name {userName}");
        }

        if (users.Count > 1)
        {
            _logger.LogError($"Multiple users found with the name {userName}");
            throw new InvalidOperationException($"Multiple users found with the name {userName}");
        }

        var user = users.Single();

        if (user.Password != password)
        {
            _logger.LogError($"Invalid password for user {userName}");
            throw new InvalidCredentialException("Invalid password.");
        }

        return user;
    }
}
