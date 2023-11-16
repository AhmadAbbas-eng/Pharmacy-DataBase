namespace Infrastructure.Entities;

public class SupplierPhone
{
    public int SupplierId { get; set; }

    public string Phone { get; set; }

    public virtual Supplier Supplier { get; set; }
}