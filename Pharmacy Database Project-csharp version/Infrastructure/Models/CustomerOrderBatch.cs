using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Entities;

public class CustomerOrderBatch
{
    public int OrderId { get; set; }
    public virtual CustomerOrder CustomerOrder { get; set; }

    [Required] 
    public int OrderAmount { get; set; }

    public int BatchId { get; set; }
    public virtual Batch Batch { get; set; }
}