using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;


public class NameMenu
{
    [Key]
    public int ProductId { get; set; }
    
    [Required]
    [StringLength(32)]
    public string ProductName { get; set; }

    [Required]
    [StringLength(32)]
    public string ProductManufacturer { get; set; }
    
    public Product Product { get; set; }
}