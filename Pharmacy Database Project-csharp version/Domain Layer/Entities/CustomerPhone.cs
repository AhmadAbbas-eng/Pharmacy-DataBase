using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class CustomerPhone
{
    [Key]
    public int CustomerId { get; set; }
    
    [Key]
    [StringLength(16)]
    public string Phone { get; set; }

    public Customer Customer { get; set; }
}