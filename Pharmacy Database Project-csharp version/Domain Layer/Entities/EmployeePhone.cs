using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

[Table("Employee_Phone")]
public class EmployeePhone
{
    [Key]
    [Column(Order = 0)]
    [StringLength(16)]
    public string Phone { get; set; }

    [Key]
    [Column(Order = 1)]
    [ForeignKey("Employee")]
    public int EmployeeId { get; set; }

    public Employee Employee { get; set; }
}