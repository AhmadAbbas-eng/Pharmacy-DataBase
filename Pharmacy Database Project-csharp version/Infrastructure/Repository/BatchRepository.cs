using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class BatchRepository : IBatchRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public BatchRepository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<BatchLifetime>> CalculateBatchLifetimeAsync()
    {
        return await _context.Batches
            .Select(b => new BatchLifetime
            {
                ProductId = b.ProductId,
                ProductName = b.Product.Name,
                ProductionDate = b.ProductionDate,
                DaysUntilExpiry = EF.Functions.DateDiffDay(DateTime.UtcNow, b.ExpiryDate)
            })
            .ToListAsync();
    }

    public async Task<IEnumerable<BatchDomain>> FindBatchesByProductionDateRangeAsync(DateTime startDate,
        DateTime endDate)
    {
        var batches = await _context.Batches
            .Where(b => b.ProductionDate >= startDate && b.ProductionDate <= endDate)
            .ToListAsync();

        return _mapper.Map<IEnumerable<BatchDomain>>(batches);
    }

    public async Task<IEnumerable<BatchDomain>> FindBatchesByExpiryDateRangeAsync(DateTime startDate, DateTime endDate)
    {
        var batches = await _context.Batches
            .Where(b => b.ExpiryDate >= startDate && b.ExpiryDate <= endDate)
            .ToListAsync();

        return _mapper.Map<IEnumerable<BatchDomain>>(batches);
    }
}