using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Entities;

public class CustomerPhone
{
    [Key] 
    public int CustomerId { get; set; }

    [Key] 
    [StringLength(16)] 
    public string Phone { get; set; }

    public Customer Customer { get; set; }
}