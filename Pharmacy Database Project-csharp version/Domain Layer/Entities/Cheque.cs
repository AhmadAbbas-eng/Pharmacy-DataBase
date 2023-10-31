using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class Cheque
{
    [Key]
    public int ChequeId { get; set; }

    [Required, StringLength(32)]
    public string BankName { get; set; }

    [Required]
    public DateTime DateOfWriting { get; set; }

    [Required]
    public DateTime DueDateOfCashing { get; set; }

    [Required]
    public int PaymentId { get; set; }

    [Required]
    public int ManagerId { get; set; }

    public virtual Payment Payment { get; set; }

    public virtual Employee Manager { get; set; }
}