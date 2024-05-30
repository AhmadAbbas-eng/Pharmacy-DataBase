namespace Infrastructure.Models;

public class SupplierPayment
{
    public int SupplierId { get; set; }

    public int ManagerId { get; set; }

    public int PaymentId { get; set; }

    public virtual Supplier Supplier { get; set; }

    public virtual Employee Manager { get; set; }

    public virtual Payment Payment { get; set; }
}