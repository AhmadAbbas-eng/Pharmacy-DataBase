using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class PaymentService : IPaymentService
{
    private readonly IPaymentRepository _paymentRepository;

    public PaymentService(IPaymentRepository paymentRepository)
    {
        _paymentRepository = paymentRepository;
    }

    public async Task ProcessPaymentsBatchAsync(IEnumerable<BatchDomain> batches)
    {
        await _paymentRepository.AddAllBatchesAsync(batches);
    }
}