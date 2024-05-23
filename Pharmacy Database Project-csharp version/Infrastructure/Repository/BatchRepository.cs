using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class BatchRepository : Repository<Batch, BatchDomain, int>, IBatchRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public BatchRepository(PharmacyDbContext context, IMapper mapper) : base(context, mapper)
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

    public async Task<IEnumerable<BatchDomain>> FindByProductionDateRangeAsync(DateTime startDate,
        DateTime endDate)
    {
        var batches = await _context.Batches
            .Where(b => b.ProductionDate >= startDate && b.ProductionDate <= endDate)
            .ToListAsync();

        return _mapper.Map<IEnumerable<BatchDomain>>(batches);
    }

    public async Task<IEnumerable<BatchDomain>> FindByExpiryDateRangeAsync(DateTime startDate, DateTime endDate)
    {
        var batches = await _context.Batches
            .Where(b => b.ExpiryDate >= startDate && b.ExpiryDate <= endDate)
            .ToListAsync();

        return _mapper.Map<IEnumerable<BatchDomain>>(batches);
    }
}