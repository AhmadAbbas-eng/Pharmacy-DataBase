using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class SupplierRepository : ISupplierRepository
{
    private readonly PharmacyDbContext _context;
    private readonly SupplierMapper _mapper;

    public SupplierRepository(PharmacyDbContext context, SupplierMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<SupplierDomain>> ListSuppliersWithDueAmountsAsync()
    {
        var suppliers = await _context.Suppliers
            .Where(s => s.Dues > 0)
            .ToListAsync();
        return suppliers.Select(s => _mapper.MapToDomain(s));
    }

    public async Task<IEnumerable<SupplierDomain>> FindByProductAsync(string productName)
    {
        var suppliers = await (from s in _context.SupplierOrders
                join p in _context.SupplierOrderBatches on s.OrderId equals p.OrderId
                join b in _context.Batches on p.BatchId equals b.BatchId
                join pr in _context.Products on b.ProductId equals pr.ProductId
                where pr.Name == productName
                select s.Supplier)
            .ToListAsync();

        return suppliers.Select(s => _mapper.MapToDomain(s));
    }

    public async Task<IEnumerable<SupplierDomain>> GetAllAsync()
    {
        var suppliers = await _context.Suppliers.ToListAsync();
        return suppliers.Select(s => _mapper.MapToDomain(s));
    }

    public async Task<SupplierDomain?> GetByIdAsync(int id)
    {
        var supplier = await _context.Suppliers.FindAsync(id);
        return supplier == null ? null : _mapper.MapToDomain(supplier);
    }

    public async Task<SupplierDomain> AddAsync(SupplierDomain supplier)
    {
        var entity = _mapper.MapToEntity(supplier);
        _context.Suppliers.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<SupplierDomain?> UpdateAsync(SupplierDomain supplier)
    {
        var entity = await _context.Suppliers.FindAsync(supplier.Id);
        if (entity == null) return null;

        _mapper.MapToEntity(supplier, entity);
        _context.Suppliers.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.Suppliers.FindAsync(id);
        if (entity == null) return false;

        _context.Suppliers.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}