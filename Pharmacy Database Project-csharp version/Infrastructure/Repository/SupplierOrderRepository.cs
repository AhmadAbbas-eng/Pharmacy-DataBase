using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class SupplierOrderRepository : ISupplierOrderRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public SupplierOrderRepository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<SupplierOrderDomain> GetByOrderIdAsync(int orderId)
    {
        var supplierOrder = await _context.SupplierOrders
            .Include(x => x.Supplier)
            .FirstOrDefaultAsync(x => x.OrderId == orderId);

        return _mapper.Map<SupplierOrderDomain>(supplierOrder);
    }
}