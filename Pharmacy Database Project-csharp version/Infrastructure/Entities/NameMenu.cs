using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Entities;

public class NameMenu
{
    [Key] public int ProductId { get; set; }

    [Required] [StringLength(32)] public string ProductName { get; set; }

    [Required] [StringLength(32)] public string ProductManufacturer { get; set; }

    public Product Product { get; set; }
}