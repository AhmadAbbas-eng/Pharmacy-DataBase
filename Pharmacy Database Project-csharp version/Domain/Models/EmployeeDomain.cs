using System.ComponentModel.DataAnnotations;

namespace Domain.Models;

public class EmployeeDomain
{
    [Key] public int EmployeeId { get; set; }
    public string Name { get; set; }
    public string NationalId { get; set; }
    public string Password { get; set; }
    public double HourlyPaid { get; set; }
    public string IsManager { get; set; }
    public string IsActive { get; set; }
}