namespace Domain.Models;

public class WorkHoursDomain
{
    public int EmployeeId { get; set; }
    public int WorkedMonth { get; set; }
    public int WorkedYear { get; set; }
    public double WorkedHours { get; set; }
    public double HourlyPaid { get; set; }
}