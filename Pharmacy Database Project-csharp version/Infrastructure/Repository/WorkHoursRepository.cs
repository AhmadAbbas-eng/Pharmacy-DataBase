using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class WorkHoursRepository : IWorkHoursRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public WorkHoursRepository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<ICollection<WorkHoursDomain>> FindWorkingHoursByEmployeeIdMonthAndYearAsync(int employeeId,
        int month, int year)
    {
        var workHours = await _context.WorkHours
            .Where(x => x.EmployeeId == employeeId && x.WorkedMonth == month && x.WorkedYear == year)
            .ToListAsync();
        return _mapper.Map<ICollection<WorkHoursDomain>>(workHours);
    }

    public async Task<ICollection<WorkHoursDomain>> FindWorkingHoursByMonthAndYearAsync(int month, int year)
    {
        var workHours = await _context.WorkHours
            .Where(x => x.WorkedMonth == month && x.WorkedYear == year)
            .ToListAsync();
        return _mapper.Map<ICollection<WorkHoursDomain>>(workHours);
    }
}