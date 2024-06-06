namespace Pharmacy.API.Dtos;

public class BatchDto
{
    public int BatchId { get; set; }
    public int ProductId { get; set; }
    public DateTime ProductionDate { get; set; }
    public DateTime ExpiryDate { get; set; }
    public int Amount { get; set; }
}