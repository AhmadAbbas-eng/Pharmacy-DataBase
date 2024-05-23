namespace Domain.Models;

public class PaymentDomain
{
    public int PaymentId { get; set; }
    public DateTime PaymentDate { get; set; }
    public double PaymentAmount { get; set; }
    public string PaymentMethod { get; set; }
}