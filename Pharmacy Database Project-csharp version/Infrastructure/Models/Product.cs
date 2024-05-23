using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Entities;

public class Product
{
    [Key] public int ProductId { get; set; }

    [Required(ErrorMessage = "The Name cannot be empty or null")]
    [StringLength(100, MinimumLength = 2, ErrorMessage = "Name must be between 2 and 100 characters.")]
    public string Name { get; set; }

    [Required(ErrorMessage = "Price is required.")]
    [Range(1, double.MaxValue, ErrorMessage = "Price must be non-zero")]
    public double Price { get; set; }

    [Required]
    [StringLength(100, MinimumLength = 2, ErrorMessage = "Manufacturer Name must be between 2 and 100 characters.")]
    public string Manufacturer { get; set; }

    public NameManufacturer NameManufacturer { get; set; }
    public ICollection<Batch> Batches { get; set; }
    public Drug Drug { get; set; }
}