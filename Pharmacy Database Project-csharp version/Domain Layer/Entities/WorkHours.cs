using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

[Table("Work_Hours")]
public class WorkHours
{
    [Key]
    [Column(Order = 0)]
    [ForeignKey("Employee")]
    public int EmployeeId { get; set; }

    [Key]
    [Column(Order = 1)]
    [Range(1, 12)]
    public int WorkedMonth { get; set; }

    [Key]
    [Column(Order = 2)]
    [Range(2000, int.MaxValue)]
    public int WorkedYear { get; set; }

    [Range(0, double.MaxValue)]
    public double WorkedHours { get; set; }

    [Range(0, double.MaxValue)]
    public double HourlyPaid { get; set; }

    public Employee Employee { get; set; }
}
