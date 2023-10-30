using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Domain_Layer.Entities;

namespace Domain_Layer.Entities;

public class CustomerOrder
{
    [Key]
    public int OrderId { get; set; }

    [Required]
    public DateTime OrderDate { get; set; }

    [Required]
    public float Price { get; set; }

    public float Discount { get; set; }

    [ForeignKey("Employee")]
    public int EmployeeId { get; set; }
    public virtual Employee Employee { get; set; }

    [ForeignKey("Customer")]
    public string CustomerId { get; set; }
    public virtual Customer Customer { get; set; }

    public virtual ICollection<CustomerOrderBatch> C_Order_Batches { get; set; }
}