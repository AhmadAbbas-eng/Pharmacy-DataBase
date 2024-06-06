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

    public async Task<IEnumerable<PendingPayment>> GetPendingPaymentsAsync()
    {
        return await _paymentRepository.GetPendingAsync();
    }

    public async Task AddAllBatchesAsync(IEnumerable<BatchDomain> batches)
    {
        await _paymentRepository.AddAllBatchesAsync(batches);
    }

    public async Task<IEnumerable<PaymentDomain>> GetAllPaymentsAsync()
    {
        return await _paymentRepository.GetAllAsync();
    }

    public async Task<PaymentDomain?> GetPaymentByIdAsync(int id)
    {
        return await _paymentRepository.GetByIdAsync(id);
    }

    public async Task<PaymentDomain> AddPaymentAsync(PaymentDomain payment)
    {
        return await _paymentRepository.AddAsync(payment);
    }

    public async Task<PaymentDomain?> UpdatePaymentAsync(PaymentDomain payment)
    {
        return await _paymentRepository.UpdateAsync(payment);
    }

    public async Task<bool> DeletePaymentAsync(int id)
    {
        return await _paymentRepository.DeleteAsync(id);
    }
}