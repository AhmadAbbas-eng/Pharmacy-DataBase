using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;

namespace Infrastructure.Repository;

public class SupplierOrderRepository : Repository<SupplierOrder, SupplierOrderDomain, int>, ISupplierOrderRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public SupplierOrderRepository(PharmacyDbContext context, IMapper mapper) : base(context, mapper)
    {
        _context = context;
        _mapper = mapper;
    }
}