using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IBatchRepository
{
    Task<IEnumerable<BatchLifetime>> CalculateLifetimeAsync();
    Task<IEnumerable<BatchDomain>> FindByProductionDateRangeAsync(DateTime startDate, DateTime endDate);
    Task<IEnumerable<BatchDomain>> FindByExpiryDateRangeAsync(DateTime startDate, DateTime endDate);

    Task<IEnumerable<BatchDomain>> GetAllAsync();
    Task<BatchDomain?> GetByIdAsync(int id);
    Task<BatchDomain> AddAsync(BatchDomain batch);
    Task<BatchDomain?> UpdateAsync(BatchDomain batch);
    Task<bool> DeleteAsync(int id);
}
