using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;


public class SupplierPayment
{
    [Key, Column(Order = 0)]
    public int SupplierId { get; set; }

    [Key, Column(Order = 1)]
    public int ManagerId { get; set; }

    [Key, Column(Order = 2)]
    public int PaymentId { get; set; }

    [ForeignKey("Supplier")]
    public virtual Supplier Supplier { get; set; }

    [ForeignKey("Manager")]
    public virtual Employee Manager { get; set; }

    [ForeignKey("Payment")]
    public virtual Payment Payment { get; set; }
}