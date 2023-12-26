namespace Domain.Models;

public class PendingPayment
{
    public int OrderId { get; set; }
    public string SupplierName { get; set; }
    public DateTime DueDateForPayment { get; set; }
    public decimal OrderCost { get; set; }
}