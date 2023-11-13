using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class SupplierRepository : Repository<Supplier, SupplierDomain, int>, ISupplierRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public SupplierRepository(PharmacyDbContext context, IMapper mapper) : base(context, mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<SupplierDomain>> ListSuppliersWithDueAmountsAsync()
    {
        var suppliers = await _context.Suppliers
            .Where(s => s.Dues > 0)
            .ToListAsync();
        return _mapper.Map<List<SupplierDomain>>(suppliers);
    }

    public async Task<IEnumerable<SupplierDomain>> FindByProductAsync(string productName)
    {
        var suppliers = await _context.Suppliers
            .Where(s => _context.SupplierOrderBatches.Any(sob => sob.Batch.Product.Name == productName))
            .ToListAsync();

        return _mapper.Map<IEnumerable<SupplierDomain>>(suppliers);
    }
}