using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;
public class Batch
{
    [Key, Column(Order = 0)]
    [ForeignKey("Product")]
    public int ProductId { get; set; }

    [Key, Column(Order = 1)]
    [DataType(DataType.Date)]
    public DateTime ProductionDate { get; set; }

    [Key, Column(Order = 2)]
    [DataType(DataType.Date)]
    public DateTime ExpiryDate { get; set; }

    [Required]
    [Range(1, int.MaxValue, ErrorMessage = "Amount must be greater than 0")]
    public int Amount { get; set; }
    
    
    public virtual Product Product { get; set; }
    public ICollection<SupplierOrderBatch> SupplierOrderBatches { get; set; }
    public ICollection<CustomerOrderBatch> CustomerOrderBatches { get; set; }
    public ICollection<DrugDisposal> DrugDisposals { get; set; }
}
