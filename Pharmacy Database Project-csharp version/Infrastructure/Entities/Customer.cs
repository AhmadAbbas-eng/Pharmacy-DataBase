using System.ComponentModel.DataAnnotations;

namespace Domain_Layer.Entities;

public class Customer
{
    [Key]
    public int CustomerId { get; set; }
    
    [StringLength(16)]
    public string CustomerNID { get; set; }

    [StringLength(32)]
    public string Name { get; set; }

    [Range(0, double.MaxValue)]
    public double Debt { get; set; } = 0.0;

    public ICollection<CustomerPhone> CustomerPhones { get; set; }
    public ICollection<CustomerOrder> CustomerOrders { get; set; }
    public ICollection<Income> Incomes { get; set; }
}
