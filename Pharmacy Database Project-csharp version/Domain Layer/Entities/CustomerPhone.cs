using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

[Table("Customer_Phone")]
public class CustomerPhone
{
    [Key]
    [Column(Order = 1)]
    [ForeignKey("Customer")]
    public int CustomerId { get; set; }
    
    [Key]
    [Column(Order = 0)]
    [StringLength(16)]
    public string Phone { get; set; }

    public Customer Customer { get; set; }
}