namespace Pharmacy.API.Dtos;

public class PendingPaymentDto
{
    public string SupplierName { get; set; }
    public DateTime DueDateForPayment { get; set; }
    public decimal OrderCost { get; set; }
}