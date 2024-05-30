using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Models;

public class Batch
{
    [Key] public int BatchId { get; set; }

    public int ProductId { get; set; }

    [DataType(DataType.Date)] public DateTime ProductionDate { get; set; }

    [DataType(DataType.Date)] public DateTime ExpiryDate { get; set; }

    [Required]
    [Range(1, int.MaxValue, ErrorMessage = "Amount must be greater than 0")]
    public int Amount { get; set; }

    public virtual Product Product { get; set; }
    public ICollection<SupplierOrderBatch> SupplierOrderBatches { get; set; }
    public ICollection<CustomerOrderBatch> CustomerOrderBatches { get; set; }
    public ICollection<DrugDisposal> DrugDisposals { get; set; }
}