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
    public void FindProducts_ReturnsCorrectProducts()
    {
        var product = CreateDomainModel();
        _repository.Add(product);
        _repository.Save();

        var foundProducts = _repository.Find(p => p.PaymentAmount == product.PaymentAmount);

        Assert.Contains(foundProducts, p => p.PaymentAmount == product.PaymentAmount);
    }

    [Fact]
    public void FindProducts_ReturnsEmpty_WhenPredicateDoesNotMatch()
    {
        var foundProducts = _repository.Find(p => p.PaymentAmount == -1);

        Assert.Empty(foundProducts);
    }
}