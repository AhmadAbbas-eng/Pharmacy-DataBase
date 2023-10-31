using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class SupplierOrderBatch
{ 
    public int OrderId { get; set; }
    public virtual SupplierOrder SupplierOrder { get; set; }

    [Required]
    public int Amount { get; set; }

    public int BatchId { get; set; }
    public Batch Batch { get; set; }
}
