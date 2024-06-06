namespace Domain.Models;

public class PaymentDomain
{
    public int Id { get; set; }

    public DateTime PaymentDate { get; set; }
    public double PaymentAmount { get; set; }
    public string PaymentMethod { get; set; }
}