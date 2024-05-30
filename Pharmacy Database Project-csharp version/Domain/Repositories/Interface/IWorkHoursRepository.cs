using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IWorkHoursRepository
{
    Task<ICollection<WorkHoursDomain>> FindWorkingHoursByEmployeeIdMonthAndYearAsync(int employeeId, int month, int year);
    Task<ICollection<WorkHoursDomain>> FindWorkingHoursByMonthAndYearAsync(int month, int year);
    Task<IEnumerable<WorkHoursDomain>> GetAllAsync();
    Task<WorkHoursDomain?> GetByIdAsync(int id);
    Task<WorkHoursDomain> AddAsync(WorkHoursDomain workHours);
    Task<WorkHoursDomain?> UpdateAsync(WorkHoursDomain workHours);
    Task<bool> DeleteAsync(int id);
}