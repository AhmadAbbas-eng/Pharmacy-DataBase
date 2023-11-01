using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Entities;

public class Tax
{
    [Key] [StringLength(16)] public string TaxId { get; set; }

    [Required] public DateTime TaxDate { get; set; }

    [Required] [Range(0, double.MaxValue)] public double TaxValue { get; set; }

    public ICollection<TaxesPayment> TaxesPayments { get; set; }
}