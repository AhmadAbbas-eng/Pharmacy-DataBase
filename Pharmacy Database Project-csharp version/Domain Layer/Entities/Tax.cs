using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class Tax
{
    [Key]
    [StringLength(16)]
    public string TaxId { get; set; }

    [Required]
    public DateTime TaxDate { get; set; }

    [Required]
    [Range(0, double.MaxValue)]
    public double TaxValue { get; set; }

    public ICollection<TaxesPayment> TaxesPayments { get; set; }
}
