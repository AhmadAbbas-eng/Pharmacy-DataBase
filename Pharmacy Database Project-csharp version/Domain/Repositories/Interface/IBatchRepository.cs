using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IBatchRepository
{
    Task<IEnumerable<BatchLifetime>> CalculateLifetimeAsync();
    Task<IEnumerable<BatchDomain>> FindByProductionDateRangeAsync(DateTime startDate, DateTime endDate);
    Task<IEnumerable<BatchDomain>> FindByExpiryDateRangeAsync(DateTime startDate, DateTime endDate);
}