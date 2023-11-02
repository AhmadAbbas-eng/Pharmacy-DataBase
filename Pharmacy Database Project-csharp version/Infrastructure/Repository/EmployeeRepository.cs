using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class EmployeeRepository : IEmployeeRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public EmployeeRepository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }
    
    public async Task<IEnumerable<EmployeeDomain>> ListEmployeesByStartDateAsync(DateTime startDate, DateTime endDate)
    {
        var employees =  await _context.Employees
            .Where(e => e.DateOfWork >= startDate && e.DateOfWork <= endDate)
            .ToListAsync();
        return _mapper.Map<List<EmployeeDomain>>(employees);
    }
    
    public async Task DeleteEmployeePhoneAsync(int employeeId, string phoneNumber)
    {
        var employeePhone = await _context.EmployeePhones
            .FirstOrDefaultAsync(ep => ep.EmployeeId == employeeId && ep.Phone == phoneNumber);
        
        if (employeePhone != null)
        {
            _context.EmployeePhones.Remove(employeePhone);
            await _context.SaveChangesAsync();
        }
    }
}