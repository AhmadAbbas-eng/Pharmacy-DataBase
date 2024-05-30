using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class SupplierOrderRepository : ISupplierOrderRepository
{
    private readonly PharmacyDbContext _context;
    private readonly SupplierOrderMapper _mapper;

    public SupplierOrderRepository(PharmacyDbContext context, SupplierOrderMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<SupplierOrderDomain> GetByOrderIdAsync(int orderId)
    {
        var supplierOrder = await _context.SupplierOrders
            .Include(x => x.Supplier)
            .FirstOrDefaultAsync(x => x.OrderId == orderId);

        return _mapper.MapToDomain(supplierOrder ?? throw new InvalidOperationException());
    }

    public async Task<IEnumerable<SupplierOrderDomain>> GetAllAsync()
    {
        var supplierOrders = await _context.SupplierOrders.Include(x => x.Supplier).ToListAsync();
        return supplierOrders.Select(x => _mapper.MapToDomain(x));
    }

    public async Task<SupplierOrderDomain?> GetByIdAsync(int id)
    {
        var supplierOrder = await _context.SupplierOrders.FindAsync(id);
        return supplierOrder == null ? null : _mapper.MapToDomain(supplierOrder);
    }

    public async Task<SupplierOrderDomain> AddAsync(SupplierOrderDomain supplierOrder)
    {
        var entity = _mapper.MapToEntity(supplierOrder);
        _context.SupplierOrders.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<SupplierOrderDomain?> UpdateAsync(SupplierOrderDomain supplierOrder)
    {
        var entity = await _context.SupplierOrders.FindAsync(supplierOrder.OrderId);
        if (entity == null) return null;

        _mapper.MapToEntity(supplierOrder, entity);
        _context.SupplierOrders.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.SupplierOrders.FindAsync(id);
        if (entity == null) return false;

        _context.SupplierOrders.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}