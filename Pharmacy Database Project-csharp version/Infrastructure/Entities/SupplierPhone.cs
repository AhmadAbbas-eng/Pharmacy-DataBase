using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class SupplierPhone
{
    public int SupplierId { get; set; }

    public string Phone { get; set; }

    public virtual Supplier Supplier { get; set; }
}
