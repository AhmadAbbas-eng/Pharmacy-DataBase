using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class PaymentService : IPaymentService
{
    private readonly IRepository<PaymentDomain, int> _paymentRepository;

    public PaymentService(IRepository<PaymentDomain, int> paymentRepository)
    {
        _paymentRepository = paymentRepository;
    }

    public async Task ProcessPaymentsBatchAsync(IEnumerable<PaymentDomain> payments)
    {
        foreach (var payment in payments) await _paymentRepository.AddAsync(payment);
        await _paymentRepository.SaveAsync();
    }
}