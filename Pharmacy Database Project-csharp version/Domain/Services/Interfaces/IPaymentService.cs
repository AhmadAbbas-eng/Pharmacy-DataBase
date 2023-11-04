using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IPaymentService
{
    Task ProcessPaymentsBatchAsync(IEnumerable<PaymentDomain> payments);
}