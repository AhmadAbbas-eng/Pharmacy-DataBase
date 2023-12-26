using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IPaymentRepository : IRepository<PaymentDomain, int>
{
    Task<IEnumerable<PendingPayment>> GetPendingAsync();
    Task AddAllBatchesAsync(IEnumerable<BatchDomain> batches);
}