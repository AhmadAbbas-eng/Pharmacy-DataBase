using System.ComponentModel.DataAnnotations;

namespace Domain.Models;

public class ChequeDomain
{
    [Key] public int ChequeId { get; set; }

    public string BankName { get; set; }

    public DateTime DateOfWriting { get; set; }

    public DateTime DueDateOfCashing { get; set; }
}