using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IPaymentRepository
{
    Task<IEnumerable<PendingPayment>> GetPendingAsync();
    Task AddAllBatchesAsync(IEnumerable<BatchDomain> batches);
    Task<IEnumerable<PaymentDomain>> GetAllAsync();
    Task<PaymentDomain?> GetByIdAsync(int id);
    Task<PaymentDomain> AddAsync(PaymentDomain payment);
    Task<PaymentDomain?> UpdateAsync(PaymentDomain payment);
    Task<bool> DeleteAsync(int id);
}