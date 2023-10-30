using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class SupplierPhone
{
    [Key, Column(Order = 0)]
    [ForeignKey("SupplierId")]
    public int SupplierId { get; set; }

    [Key, Column(Order = 1), MaxLength(16)]
    public string Phone { get; set; }

    public virtual Supplier Supplier { get; set; }
}
