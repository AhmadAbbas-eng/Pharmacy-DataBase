using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class WorkHoursRepository : IWorkHoursRepository
{
    private readonly PharmacyDbContext _context;
    private readonly WorkHoursMapper _mapper;

    public WorkHoursRepository(PharmacyDbContext context, WorkHoursMapper mapper)
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
        return workHours.Select(x => _mapper.MapToDomain(x)).ToList();
    }

    public async Task<ICollection<WorkHoursDomain>> FindWorkingHoursByMonthAndYearAsync(int month, int year)
    {
        var workHours = await _context.WorkHours
            .Where(x => x.WorkedMonth == month && x.WorkedYear == year)
            .ToListAsync();
        return workHours.Select(x => _mapper.MapToDomain(x)).ToList();
    }

    public async Task<IEnumerable<WorkHoursDomain>> GetAllAsync()
    {
        var workHours = await _context.WorkHours.ToListAsync();
        return workHours.Select(x => _mapper.MapToDomain(x));
    }

    public async Task<WorkHoursDomain?> GetByIdAsync(int id)
    {
        var workHours = await _context.WorkHours.FindAsync(id);
        return workHours == null ? null : _mapper.MapToDomain(workHours);
    }

    public async Task<WorkHoursDomain> AddAsync(WorkHoursDomain workHours)
    {
        var entity = _mapper.MapToEntity(workHours);
        _context.WorkHours.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<WorkHoursDomain?> UpdateAsync(WorkHoursDomain workHours)
    {
        var entity = await _context.WorkHours.FindAsync(workHours.Id);
        if (entity == null) return null;

        _mapper.MapToEntity(workHours, entity);
        _context.WorkHours.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.WorkHours.FindAsync(id);
        if (entity == null) return false;

        _context.WorkHours.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}