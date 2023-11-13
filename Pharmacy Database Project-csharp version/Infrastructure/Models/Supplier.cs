using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Entities;

public class Supplier
{
    [Key] public int SupplierId { get; set; }

    [Required] [StringLength(32)] public string Name { get; set; }

    [StringLength(32)] public string Address { get; set; }

    [StringLength(64)] [EmailAddress] public string? Email { get; set; }

    [Range(0, double.MaxValue)]
    [DefaultValue(0.0f)]
    public double Dues { get; set; } = 0.0;

    public virtual ICollection<SupplierOrder> SupplierOrders { get; set; }
    public virtual ICollection<SupplierPhone> SupplierPhones { get; set; }
    public virtual ICollection<SupplierPayment> SupplierPayments { get; set; }
}