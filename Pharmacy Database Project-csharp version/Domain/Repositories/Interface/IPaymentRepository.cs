using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IPaymentRepository
{
    IEnumerable<PendingPayment> GetPendingPayments();
}