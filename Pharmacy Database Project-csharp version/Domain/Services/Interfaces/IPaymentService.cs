using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IPaymentService
{
    Task<IEnumerable<PendingPayment>> GetPendingPaymentsAsync();
    Task AddAllBatchesAsync(IEnumerable<BatchDomain> batches);
    Task<IEnumerable<PaymentDomain>> GetAllPaymentsAsync();
    Task<PaymentDomain?> GetPaymentByIdAsync(int id);
    Task<PaymentDomain> AddPaymentAsync(PaymentDomain payment);
    Task<PaymentDomain?> UpdatePaymentAsync(PaymentDomain payment);
    Task<bool> DeletePaymentAsync(int id);
}