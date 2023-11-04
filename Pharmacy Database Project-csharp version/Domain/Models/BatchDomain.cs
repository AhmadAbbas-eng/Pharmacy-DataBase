using System.ComponentModel.DataAnnotations;

namespace Domain.Models;

public class BatchDomain
{
    [Key] public int BatchId { get; set; }

    public int ProductId { get; set; }

    public DateTime ProductionDate { get; set; }

    public DateTime ExpiryDate { get; set; }
    
    public int Amount { get; set; }
}