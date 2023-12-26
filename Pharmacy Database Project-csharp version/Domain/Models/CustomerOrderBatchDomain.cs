namespace Domain.Models;
public class CustomerOrderBatchDomain
{
    public int OrderId { get; set; }
    public int OrderAmount { get; set; }
    public int BatchId { get; set; }
}