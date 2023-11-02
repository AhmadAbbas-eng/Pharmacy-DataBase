using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IPaymentRepository
{
    Task<IEnumerable<PendingPayment>> GetPendingPaymentsAsync();
}