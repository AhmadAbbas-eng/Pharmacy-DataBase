using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class PaymentRepository : IPaymentRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;
    
    public PaymentRepository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }
    
    public async Task<IEnumerable<PendingPayment>> GetPendingPaymentsAsync()
    {
        DateTime thirtyDaysFromNow = DateTime.Now.AddDays(30);
    
        var pendingPayments = await _context.SupplierOrders
            .Include(o => o.Supplier) 
            .Where(o => o.DueDateForPayment <= thirtyDaysFromNow)
            .ToListAsync();

        var pendingPayment= _mapper.Map<List<PendingPayment>>(pendingPayments);

        return pendingPayment;
    }
}