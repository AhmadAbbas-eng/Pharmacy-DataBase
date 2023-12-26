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
        _repository.Add(payment);
        _repository.Save();

        var paymentAmount = payment.PaymentAmount;
        var foundProducts = _repository.Find(p => p.PaymentAmount == paymentAmount);

        Assert.Contains(foundProducts, p => p.PaymentAmount == payment.PaymentAmount);
    }

    [Fact]
    public void FindPayments_ReturnsEmpty_WhenPredicateDoesNotMatch()
    {
        var foundPayment = _repository.Find(p => p.PaymentAmount == -1);

        Assert.Empty(foundPayment);
    }
}