using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IWorkHoursService
{
    Task<double> CalculateMonthlyWagesAsync(int employeeId, int month, int year);
    Task<ICollection<WorkHoursDomain>> GenerateMonthlyWorkReportsAsync(int month, int year);

    Task<ICollection<WorkHoursDomain>>
        GetWorkingHoursByEmployeeIdMonthAndYearAsync(int employeeId, int month, int year);

    Task<ICollection<WorkHoursDomain>> GetWorkingHoursByMonthAndYearAsync(int month, int year);
    Task<IEnumerable<WorkHoursDomain>> GetAllWorkHoursAsync();
    Task<WorkHoursDomain?> GetWorkHoursByIdAsync(int id);
    Task<WorkHoursDomain> AddWorkHoursAsync(WorkHoursDomain workHours);
    Task<WorkHoursDomain?> UpdateWorkHoursAsync(WorkHoursDomain workHours);
    Task<bool> DeleteWorkHoursAsync(int id);
}