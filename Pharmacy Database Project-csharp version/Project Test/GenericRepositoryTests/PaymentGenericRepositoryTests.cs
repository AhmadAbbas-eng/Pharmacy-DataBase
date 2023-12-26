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
    public void FindPayment_ReturnsCorrectPayments()
    {
        var payment = CreateDomainModel();
        _repository.AddAsync(payment);
        _repository.SaveAsync();

        var paymentAmount = payment.PaymentAmount;
        var foundProducts = _repository.FindAsync(p => p.PaymentAmount == paymentAmount).Result;

        Assert.Contains(foundProducts, p => p.PaymentAmount == payment.PaymentAmount);
    }

    [Fact]
    public void FindPayments_ReturnsEmpty_WhenPredicateDoesNotMatch()
    {
        var foundPayment = _repository.FindAsync(p => p.PaymentAmount == -1).Result;

        Assert.Empty(foundPayment);
    }
}