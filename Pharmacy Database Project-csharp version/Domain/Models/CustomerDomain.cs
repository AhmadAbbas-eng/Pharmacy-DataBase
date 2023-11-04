using System.ComponentModel.DataAnnotations;

namespace Domain.Models;

public class CustomerDomain
{
    [Key] public int CustomerId { get; set; }
    public string Name { get; set; }
    public double Debt { get; set; } = 0.0;
}