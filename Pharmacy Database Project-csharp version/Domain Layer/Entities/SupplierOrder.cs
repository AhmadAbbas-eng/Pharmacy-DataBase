using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;


public class SupplierOrder
{
    [Key]
    public int OrderID { get; set; }

    [Required]
    public DateTime DateOfOrder { get; set; }

    [Required]
    public float OrderCost { get; set; }

    public float OrderDiscount { get; set; }

    public DateTime DueDateForPayment { get; set; }

    [ForeignKey("Supplier")]
    public int SupplierId { get; set; }
    public virtual Supplier Supplier { get; set; }

    [ForeignKey("Manager")]
    public int ManagerId { get; set; }
    public virtual Employee Manager { get; set; }

    [ForeignKey("Receiver")]
    public int RecievedBy { get; set; }
    public virtual Employee Receiver { get; set; }

    public DateTime? RecievedDate { get; set; }

    public virtual ICollection<SupplierOrderBatch> SupplierOrderBatches { get; set; }
}