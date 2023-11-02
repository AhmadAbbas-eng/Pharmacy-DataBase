using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class WorkHoursService : IWorkHoursService
{
    private readonly IRepository<DomainWorkHours, int> _workHoursRepository;

    public WorkHoursService(IRepository<DomainWorkHours, int> workHoursRepository)
    {
        _workHoursRepository = workHoursRepository;
    }

    public async Task<double> CalculateMonthlyWagesAsync(int employeeId, int month, int year)
    {
        var workHours = await _workHoursRepository.FindAsync(wh => wh.EmployeeId == employeeId && wh.WorkedMonth == month && wh.WorkedYear == year);
        return workHours.Sum(wh => wh.WorkedHours * wh.HourlyPaid);
    }

    public async Task<IEnumerable<DomainWorkHours>> GenerateMonthlyWorkReportsAsync(int month, int year)
    {
        var workHours = await _workHoursRepository.FindAsync(wh => wh.WorkedMonth == month && wh.WorkedYear == year);
        return workHours;
    }
}
