namespace Domain.Models;

public class TaxDomain
{
    public string Id { get; set; }

    public DateTime TaxDate { get; set; }
    public double TaxValue { get; set; }
}