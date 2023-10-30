using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class TaxesPayment
{
    [Key, Column(Order = 0)]
    public int PaymentId { get; set; }

    [Key, Column(Order = 1), MaxLength(16)]
    public string TaxId { get; set; }

    [Key, Column(Order = 2)]
    public int ManagerId { get; set; }

    [ForeignKey("Manager")]
    public virtual Employee Manager { get; set; }

    [ForeignKey("Payment")]
    public virtual Payment Payment { get; set; }

    [ForeignKey("Tax")]
    public virtual Tax Tax { get; set; }
}