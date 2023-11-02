using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IWorkHoursService
{
    Task<double> CalculateMonthlyWagesAsync(int employeeId, int month, int year);
    Task<IEnumerable<DomainWorkHours>> GenerateMonthlyWorkReportsAsync(int month, int year);
}