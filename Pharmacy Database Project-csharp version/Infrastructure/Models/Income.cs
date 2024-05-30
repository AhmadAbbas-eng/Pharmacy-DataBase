using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Models;

public class Income
{
    [Key] public int IncomeId { get; set; }

    [Required] public int Amount { get; set; }

    [Required] public DateTime IncomeDate { get; set; }

    public int EmployeeId { get; set; }
    public virtual Employee Employee { get; set; }

    public int CustomerId { get; set; }
    public virtual Customer Customer { get; set; }
}