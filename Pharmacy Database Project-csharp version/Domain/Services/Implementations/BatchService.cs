using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class BatchService : IBatchService
{
    private readonly IBatchRepository _batchRepository;

    public BatchService(IBatchRepository batchRepository)
    {
        _batchRepository = batchRepository;
    }

    public async Task<IEnumerable<BatchLifetime>> CalculateBatchLifetimeAsync()
    {
        return await _batchRepository.CalculateLifetimeAsync();
    }

    public async Task<IEnumerable<BatchDomain>> GetBatchesByProductionDateRangeAsync(DateTime startDate,
        DateTime endDate)
    {
        return await _batchRepository.FindByProductionDateRangeAsync(startDate, endDate);
    }

    public async Task<IEnumerable<BatchDomain>> GetBatchesByExpiryDateRangeAsync(DateTime startDate, DateTime endDate)
    {
        return await _batchRepository.FindByExpiryDateRangeAsync(startDate, endDate);
    }

    public async Task<IEnumerable<BatchDomain>> GetAllBatchesAsync()
    {
        return await _batchRepository.GetAllAsync();
    }

    public async Task<BatchDomain?> GetBatchByIdAsync(int id)
    {
        return await _batchRepository.GetByIdAsync(id);
    }

    public async Task<BatchDomain> AddBatchAsync(BatchDomain batch)
    {
        return await _batchRepository.AddAsync(batch);
    }

    public async Task<BatchDomain?> UpdateBatchAsync(BatchDomain batch)
    {
        return await _batchRepository.UpdateAsync(batch);
    }

    public async Task<bool> DeleteBatchAsync(int id)
    {
        return await _batchRepository.DeleteAsync(id);
    }
}