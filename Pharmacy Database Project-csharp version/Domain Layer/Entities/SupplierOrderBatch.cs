using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class SupplierOrderBatch
{
    [Key, Column(Order = 0)]
    [ForeignKey("SupplierOrder")]
    public int OrderId { get; set; }
    public virtual SupplierOrder SupplierOrder { get; set; }

    [Key, Column(Order = 1)]
    [ForeignKey("Product")]
    public int ProductId { get; set; }
    public virtual Product Product { get; set; }

    [Key, Column(Order = 2)]
    public DateTime ProductionDate { get; set; }

    [Key, Column(Order = 3)]
    public DateTime ExpiryDate { get; set; }

    [Required]
    public int Amount { get; set; }
}
