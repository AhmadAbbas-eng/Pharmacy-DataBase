using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Models;

public class EmployeePhone
{
    [Key] [StringLength(16)] public string Phone { get; set; }

    [Key] public int EmployeeId { get; set; }

    public Employee Employee { get; set; }
}