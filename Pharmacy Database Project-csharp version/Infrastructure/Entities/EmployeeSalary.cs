using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class EmployeeSalary
{
    public int ManagerId { get; set; }

    public int EmployeeId { get; set; }

    public int PaymentId { get; set; }

    public virtual Employee Employee { get; set; }

    public virtual Employee Manager { get; set; }
    public virtual Payment Payment { get; set; }
}