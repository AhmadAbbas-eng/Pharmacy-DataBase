using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IBatchRepository : IRepository<BatchDomain, int>
{
    Task<IEnumerable<BatchLifetime>> CalculateBatchLifetimeAsync();
    Task<IEnumerable<BatchDomain>> FindBatchesByProductionDateRangeAsync(DateTime startDate, DateTime endDate);
    Task<IEnumerable<BatchDomain>> FindBatchesByExpiryDateRangeAsync(DateTime startDate, DateTime endDate);
}