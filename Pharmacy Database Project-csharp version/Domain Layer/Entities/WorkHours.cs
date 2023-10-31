using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class WorkHours
{
    [Key]
    public int EmployeeId { get; set; }

    [Key]
    [Range(1, 12)]
    public int WorkedMonth { get; set; }

    [Key]
    [Range(2000, int.MaxValue)]
    public int WorkedYear { get; set; }

    [Range(0, double.MaxValue)]
    public double WorkedHours { get; set; }

    [Range(0, double.MaxValue)]
    public double HourlyPaid { get; set; }

    public Employee Employee { get; set; }
}
