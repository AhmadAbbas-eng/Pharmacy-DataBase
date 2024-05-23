namespace Domain.Models;

public class CustomerOrderDomain
{
    public int OrderId { get; set; }
    public DateTime OrderDate { get; set; }
    public float Price { get; set; }
    public float Discount { get; set; }
    public int EmployeeId { get; set; }
    public int CustomerId { get; set; }
}