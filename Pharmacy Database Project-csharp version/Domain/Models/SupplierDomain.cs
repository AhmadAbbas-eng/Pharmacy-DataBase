using Domain.Repositories.Interface;

namespace Domain.Models;

public class SupplierDomain
{
    public string Name { get; set; }

    public string Address { get; set; }

    public string? Email { get; set; }
    
    public double Dues { get; set; } = 0.0;
    public ICollection<SupplierPhoneDomain> SupplierPhones { get; set; }
}