using Domain.Models;
using Infrastructure;
using Infrastructure.Entities;
using Infrastructure.Repository;

namespace Project_Test.GenericRepositoryUnitTests;

public class PaymentGenericRepositoryTests : BaseGenericRepositoryTests<PharmacyDbContext, Payment, PaymentDomain, int>
{
    public PaymentGenericRepositoryTests()
    {
        _repository = new Repository<Payment, PaymentDomain, int>(_context, _mapper);
    }

    [Fact]
    public async Task FindPayment_ReturnsCorrectPayments()
    {
        var payment = CreateDomainModel();
        await _repository.AddAsync(payment);
        await _repository.SaveAsync();

        var paymentAmount = payment.PaymentAmount;
        var foundProducts = await _repository.FindAsync(p => p.PaymentAmount == paymentAmount);

        Assert.Contains(foundProducts, p => p.PaymentAmount == payment.PaymentAmount);
    }

    [Fact]
    public async Task FindPayments_ReturnsEmpty_WhenPredicateDoesNotMatch()
    {
        var foundPayment = await _repository.FindAsync(p => p.PaymentAmount == -1);

        Assert.Empty(foundPayment);
    }
}