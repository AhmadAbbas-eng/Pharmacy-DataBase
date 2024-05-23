using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IWorkHoursService
{
    Task<double> CalculateMonthlyWagesAsync(int employeeId, int month, int year);
    Task<ICollection<WorkHoursDomain>> GenerateMonthlyWorkReportsAsync(int month, int year);
}