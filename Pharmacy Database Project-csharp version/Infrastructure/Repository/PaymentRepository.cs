using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class PaymentRepository : IPaymentRepository
{
    private readonly PharmacyDbContext _context;
    private readonly PaymentMapper _mapper;

    public PaymentRepository(PharmacyDbContext context, PaymentMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<PendingPayment>> GetPendingAsync()
    {
        var thirtyDaysFromNow = DateTime.Now.AddDays(30);

        var pendingPayments = await _context.SupplierOrders
            .Include(o => o.Supplier)
            .Where(o => o.DueDateForPayment <= thirtyDaysFromNow)
            .ToListAsync();

        return pendingPayments.Select(o => _mapper.MapToPendingPayment(o));
    }

    public async Task AddAllBatchesAsync(IEnumerable<BatchDomain> batches)
    {
        var entities = batches.Select(b => _mapper.MapToEntity(b));
        await _context.Batches.AddRangeAsync(entities);
        await _context.SaveChangesAsync();
    }

    public async Task<IEnumerable<PaymentDomain>> GetAllAsync()
    {
        var payments = await _context.Payments.ToListAsync();
        return payments.Select(p => _mapper.MapToDomain(p));
    }

    public async Task<PaymentDomain?> GetByIdAsync(int id)
    {
        var payment = await _context.Payments.FindAsync(id);
        return payment == null ? null : _mapper.MapToDomain(payment);
    }

    public async Task<PaymentDomain> AddAsync(PaymentDomain payment)
    {
        var entity = _mapper.MapToEntity(payment);
        _context.Payments.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<PaymentDomain?> UpdateAsync(PaymentDomain payment)
    {
        var entity = await _context.Payments.FindAsync(payment.Id);
        if (entity == null) return null;

        _mapper.MapToEntity(payment, entity);
        _context.Payments.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.Payments.FindAsync(id);
        if (entity == null) return false;

        _context.Payments.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}