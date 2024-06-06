using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class WorkHoursService : IWorkHoursService
{
    private readonly IWorkHoursRepository _workHoursRepository;

    public WorkHoursService(IWorkHoursRepository workHoursRepository)
    {
        _workHoursRepository = workHoursRepository;
    }

    public async Task<double> CalculateMonthlyWagesAsync(int employeeId, int month, int year)
    {
        var workHours =
            await _workHoursRepository.FindWorkingHoursByEmployeeIdMonthAndYearAsync(employeeId, month, year);
        return workHours.Sum(wh => wh.WorkedHours * wh.HourlyPaid);
    }

    public async Task<ICollection<WorkHoursDomain>> GenerateMonthlyWorkReportsAsync(int month, int year)
    {
        var workHours = await _workHoursRepository.FindWorkingHoursByMonthAndYearAsync(month, year);
        return workHours;
    }

    public async Task<ICollection<WorkHoursDomain>> GetWorkingHoursByEmployeeIdMonthAndYearAsync(int employeeId,
        int month, int year)
    {
        return await _workHoursRepository.FindWorkingHoursByEmployeeIdMonthAndYearAsync(employeeId, month, year);
    }

    public async Task<ICollection<WorkHoursDomain>> GetWorkingHoursByMonthAndYearAsync(int month, int year)
    {
        return await _workHoursRepository.FindWorkingHoursByMonthAndYearAsync(month, year);
    }

    public async Task<IEnumerable<WorkHoursDomain>> GetAllWorkHoursAsync()
    {
        return await _workHoursRepository.GetAllAsync();
    }

    public async Task<WorkHoursDomain?> GetWorkHoursByIdAsync(int id)
    {
        return await _workHoursRepository.GetByIdAsync(id);
    }

    public async Task<WorkHoursDomain> AddWorkHoursAsync(WorkHoursDomain workHours)
    {
        return await _workHoursRepository.AddAsync(workHours);
    }

    public async Task<WorkHoursDomain?> UpdateWorkHoursAsync(WorkHoursDomain workHours)
    {
        return await _workHoursRepository.UpdateAsync(workHours);
    }

    public async Task<bool> DeleteWorkHoursAsync(int id)
    {
        return await _workHoursRepository.DeleteAsync(id);
    }
}