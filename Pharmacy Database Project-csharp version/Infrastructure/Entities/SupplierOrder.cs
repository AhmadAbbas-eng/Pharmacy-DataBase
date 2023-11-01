using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;


public class SupplierOrder
{
    [Key]
    public int OrderId { get; set; }

    [Required]
    public DateTime DateOfOrder { get; set; }

    [Required]
    public float OrderCost { get; set; }

    public float OrderDiscount { get; set; }

    public DateTime DueDateForPayment { get; set; }
    
    public int SupplierId { get; set; }
    public virtual Supplier Supplier { get; set; }

    public int ManagerId { get; set; }
    public virtual Employee Manager { get; set; }

    public int ReceiverId { get; set; }
    public DateTime? ReceivedDate { get; set; }

    public virtual Employee Receiver { get; set; }

    public virtual ICollection<SupplierOrderBatch> SupplierOrderBatches { get; set; }
}