using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class CustomerOrderBatch
{
    [Key, Column(Order = 0)]
    [ForeignKey("Order")]
    public int OrderId { get; set; }

    [Key, Column(Order = 1)]
    public int ProductId { get; set; }

    [Key, Column(Order = 2)]
    public DateTime BatchProductionDate { get; set; }

    [Key, Column(Order = 3)]
    public DateTime BatchExpiryDate { get; set; }

    [Required]
    public int OrderAmount { get; set; }

    public virtual CustomerOrder CustomerOrder { get; set; }

    [ForeignKey("Product, BatchProductionDate, BatchExpiryDate")]
    public virtual Batch Batch { get; set; }
}