namespace Domain.Models;

public class SupplierDomain
{
    public int SupplierId { get; set; }
    public string Name { get; set; }
    public string Address { get; set; }
    public string? Email { get; set; }
    public double Dues { get; set; }
}