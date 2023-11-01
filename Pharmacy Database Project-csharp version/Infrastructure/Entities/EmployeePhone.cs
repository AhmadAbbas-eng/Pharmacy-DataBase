using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class EmployeePhone
{
    [Key]
    [StringLength(16)]
    public string Phone { get; set; }

    [Key]
    public int EmployeeId { get; set; }

    public Employee Employee { get; set; }
}