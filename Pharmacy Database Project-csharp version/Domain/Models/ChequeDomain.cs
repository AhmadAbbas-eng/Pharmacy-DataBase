namespace Domain.Models;

public class ChequeDomain
{
    public int ChequeId { get; set; }
    public string BankName { get; set; }
    public DateTime DateOfWriting { get; set; }
    public DateTime DueDateOfCashing { get; set; }
    public int PaymentId { get; set; }
    public int ManagerId { get; set; }
}