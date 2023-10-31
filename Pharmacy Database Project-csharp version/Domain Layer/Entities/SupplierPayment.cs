using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;


public class SupplierPayment
{
    public int SupplierId { get; set; }

    public int ManagerId { get; set; }

    public int PaymentId { get; set; }

    public virtual Supplier Supplier { get; set; }

    public virtual Employee Manager { get; set; }

    public virtual Payment Payment { get; set; }
}
