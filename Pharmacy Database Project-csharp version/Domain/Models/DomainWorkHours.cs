namespace Domain.Models;

public class DomainWorkHours
{
    public int EmployeeId { get; set; }
    public string Name { get; set; }
    public int WorkedMonth { get; set; }

    public int WorkedYear { get; set; }

    public double WorkedHours { get; set; }

    public double HourlyPaid { get; set; }
    

}