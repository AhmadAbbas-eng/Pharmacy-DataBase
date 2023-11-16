using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Infrastructure.Entities;

public class Tax
{
    [Key]
    [MaxLength]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public string TaxId { get; set; }

    [Required] 
    public DateTime TaxDate { get; set; }

    [Required] 
    [Range(0, double.MaxValue)] 
    public double TaxValue { get; set; }

    public ICollection<TaxesPayment> TaxesPayments { get; set; }
}