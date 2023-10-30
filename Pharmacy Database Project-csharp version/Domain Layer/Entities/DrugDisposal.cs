using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class DrugDisposal
{
    [Key]
    public int DisposalId { get; set; }

    [Required]
    public int Amount { get; set; }

    [Required]
    public DateTime DisposalDate { get; set; }

    [ForeignKey("Employee")]
    public int EmployeeId { get; set; }
    public virtual Employee Employee { get; set; }

    [ForeignKey("Payment")]
    public int PaymentId { get; set; }
    public virtual Payment Payment { get; set; }

    [ForeignKey("Product")]
    public int ProductId { get; set; }
    public virtual Product Product { get; set; }

    [ForeignKey("Batch")]
    [Column("BatchProductionDate", Order = 0)]
    public DateTime BatchProductionDate { get; set; }
    
    [ForeignKey("Batch")]
    [Column("BatchExpiryDate", Order = 1)]
    public DateTime BatchExpiryDate { get; set; }

    [ForeignKey("Product, BatchProductionDate, BatchExpiryDate")]
    public virtual Batch Batch { get; set; }
}
