using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class BatchRepository : IBatchRepository
{
    private readonly PharmacyDbContext _context;
    private readonly BatchMapper _mapper;

    public BatchRepository(PharmacyDbContext context, BatchMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<BatchLifetime>> CalculateLifetimeAsync()
    {
        return await _context.Batches
            .Select(b => new BatchLifetime
            (
                b.ProductId,
                b.Product.Name,
                b.ProductionDate,
                EF.Functions.DateDiffDay(DateTime.UtcNow, b.ExpiryDate)
            ))
            .ToListAsync();
    }

    public async Task<IEnumerable<BatchDomain>> FindByProductionDateRangeAsync(DateTime startDate, DateTime endDate)
    {
        var batches = await _context.Batches
            .Where(b => b.ProductionDate >= startDate && b.ProductionDate <= endDate)
            .ToListAsync();

        return batches.Select(b => _mapper.MapToDomain(b));
    }

    public async Task<IEnumerable<BatchDomain>> FindByExpiryDateRangeAsync(DateTime startDate, DateTime endDate)
    {
        var batches = await _context.Batches
            .Where(b => b.ExpiryDate >= startDate && b.ExpiryDate <= endDate)
            .ToListAsync();

        return batches.Select(b => _mapper.MapToDomain(b));
    }

    public async Task<IEnumerable<BatchDomain>> GetAllAsync()
    {
        var batches = await _context.Batches.ToListAsync();
        return batches.Select(b => _mapper.MapToDomain(b));
    }

    public async Task<BatchDomain?> GetByIdAsync(int id)
    {
        var batch = await _context.Batches.FindAsync(id);
        return batch == null ? null : _mapper.MapToDomain(batch);
    }

    public async Task<BatchDomain> AddAsync(BatchDomain batch)
    {
        var entity = _mapper.MapToEntity(batch);
        _context.Batches.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<BatchDomain?> UpdateAsync(BatchDomain batch)
    {
        var entity = await _context.Batches.FindAsync(batch.BatchId);
        if (entity == null) return null;

        _mapper.MapToEntity(batch, entity);
        _context.Batches.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.Batches.FindAsync(id);
        if (entity == null) return false;

        _context.Batches.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}
