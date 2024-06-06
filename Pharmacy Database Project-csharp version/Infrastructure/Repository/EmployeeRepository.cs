using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class EmployeeRepository : IEmployeeRepository
{
    private readonly PharmacyDbContext _context;
    private readonly EmployeeMapper _mapper;

    public EmployeeRepository(PharmacyDbContext context, EmployeeMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<EmployeeDomain>> ListByStartDateAsync(DateTime startDate, DateTime endDate)
    {
        var employees = await _context.Employees
            .Where(e => e.DateOfWork >= startDate && e.DateOfWork <= endDate)
            .ToListAsync();
        return employees.Select(e => _mapper.MapToDomain(e));
    }

    public async Task DeletePhoneAsync(int employeeId, string phoneNumber)
    {
        var employeePhone = await _context.EmployeePhones
            .FirstOrDefaultAsync(ep => ep.EmployeeId == employeeId && ep.Phone == phoneNumber);

        if (employeePhone != null)
        {
            _context.EmployeePhones.Remove(employeePhone);
            await _context.SaveChangesAsync();
        }
    }

    public async Task<IEnumerable<EmployeeDomain>> GetAllAsync()
    {
        var employees = await _context.Employees.ToListAsync();
        return employees.Select(e => _mapper.MapToDomain(e));
    }

    public async Task<EmployeeDomain?> GetByIdAsync(int id)
    {
        var employee = await _context.Employees.FindAsync(id);
        return employee == null ? null : _mapper.MapToDomain(employee);
    }

    public async Task<EmployeeDomain> AddAsync(EmployeeDomain employee)
    {
        var entity = _mapper.MapToEntity(employee);
        _context.Employees.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<EmployeeDomain?> UpdateAsync(EmployeeDomain employee)
    {
        var entity = await _context.Employees.FindAsync(employee.Id);
        if (entity == null) return null;

        _mapper.MapToEntity(employee, entity);
        _context.Employees.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.Employees.FindAsync(id);
        if (entity == null) return false;

        _context.Employees.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}