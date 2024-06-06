namespace Pharmacy.API.Dtos;

public class BatchLifetimeDto
{
    public int ProductId { get; set; }
    public string ProductName { get; set; }
    public DateTime ProductionDate { get; set; }
    public int DaysUntilExpiry { get; set; }
}