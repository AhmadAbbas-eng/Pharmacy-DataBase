using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IBatchService
{
    Task<IEnumerable<BatchLifetime>> CalculateBatchLifetimeAsync();
    Task<IEnumerable<BatchDomain>> GetBatchesByProductionDateRangeAsync(DateTime startDate, DateTime endDate);
    Task<IEnumerable<BatchDomain>> GetBatchesByExpiryDateRangeAsync(DateTime startDate, DateTime endDate);

    Task<IEnumerable<BatchDomain>> GetAllBatchesAsync();
    Task<BatchDomain?> GetBatchByIdAsync(int id);
    Task<BatchDomain> AddBatchAsync(BatchDomain batch);
    Task<BatchDomain?> UpdateBatchAsync(BatchDomain batch);
    Task<bool> DeleteBatchAsync(int id);
}