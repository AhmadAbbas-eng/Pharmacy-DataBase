using System.ComponentModel.DataAnnotations;

namespace Domain.Models;

public class TaxDomain
{
    [Key] public string TaxId { get; set; }

    public DateTime TaxDate { get; set; }

    public double TaxValue { get; set; }
}