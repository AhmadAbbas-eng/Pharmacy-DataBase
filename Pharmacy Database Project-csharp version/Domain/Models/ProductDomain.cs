using System.ComponentModel.DataAnnotations;

namespace Domain.Models;

public class ProductDomain
{
    [Key] public int ProductId { get; set; }
    public string Name { get; set; }

    public double Price { get; set; }

    public string Manufacturer { get; set; }
}