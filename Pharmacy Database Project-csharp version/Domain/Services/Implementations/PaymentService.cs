using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class PaymentService : IPaymentService
{
    private readonly IRepository<ChequeDomain, int> _chequeRepository;
    private readonly IRepository<PaymentDomain, int> _paymentRepository;

    public PaymentService(IRepository<PaymentDomain, int> paymentRepository,
        IRepository<ChequeDomain, int> chequeRepository)
    {
        _paymentRepository = paymentRepository;
        _chequeRepository = chequeRepository;
    }

    public async Task ProcessPaymentsBatchAsync(IEnumerable<PaymentDomain> payments)
    {
        foreach (var payment in payments) await _paymentRepository.AddAsync(payment);
        await _paymentRepository.SaveAsync();
    }
}