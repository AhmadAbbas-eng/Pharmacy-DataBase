using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IPaymentService
{
    Task ProcessPaymentsBatchAsync(IEnumerable<BatchDomain> batches);
}