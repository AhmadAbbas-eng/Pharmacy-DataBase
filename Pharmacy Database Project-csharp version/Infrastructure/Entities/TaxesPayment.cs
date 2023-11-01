using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class TaxesPayment
{
    public int PaymentId { get; set; }

    public string TaxId { get; set; }

    public int ManagerId { get; set; }

    public virtual Employee Manager { get; set; }

    public virtual Payment Payment { get; set; }

    public virtual Tax Tax { get; set; }
}