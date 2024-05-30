using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Models;

public class CustomerOrder
{
    [Key] public int OrderId { get; set; }

    [Required] public DateTime OrderDate { get; set; }

    [Required] public float Price { get; set; }

    public float Discount { get; set; }

    public int EmployeeId { get; set; }
    public virtual Employee Employee { get; set; }

    public int CustomerId { get; set; }
    public virtual Customer Customer { get; set; }

    public virtual ICollection<CustomerOrderBatch> CustomerOrderBatches { get; set; }
}