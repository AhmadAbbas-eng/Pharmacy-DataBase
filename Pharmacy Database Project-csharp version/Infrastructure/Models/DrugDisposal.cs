using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Entities;

public class DrugDisposal
{
    [Key] 
    public int DisposalId { get; set; }

    [Required]
    public int Amount { get; set; }

    [Required] 
    public DateTime DisposalDate { get; set; }

    public int EmployeeId { get; set; }
    public virtual Employee Employee { get; set; }

    public int PaymentId { get; set; }
    public virtual Payment Payment { get; set; }

    public int BatchId { get; set; }
    public virtual Batch Batch { get; set; }
}