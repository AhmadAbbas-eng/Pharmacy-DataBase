using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IWorkHoursRepository
{
    Task<ICollection<WorkHoursDomain>> FindWorkingHoursByEmployeeIdMonthAndYearAsync(int employeeId, int month,
        int year);

    Task<ICollection<WorkHoursDomain>> FindWorkingHoursByMonthAndYearAsync(int month, int year);
}