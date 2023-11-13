namespace Domain.Models;
public class DrugDisposalDomain
{
    public int DisposalId { get; set; }
    public int Amount { get; set; }
    public DateTime DisposalDate { get; set; }
    public int EmployeeId { get; set; }
    public int PaymentId { get; set; }
    public int BatchId { get; set; }
}