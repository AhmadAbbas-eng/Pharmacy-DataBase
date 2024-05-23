using Domain.Models;
using Infrastructure;
using Infrastructure.Entities;
using Infrastructure.Repository;

namespace Project_Test.GenericRepositoryUnitTests;

public class ProductGenericRepositoryTests : BaseGenericRepositoryTests<PharmacyDbContext, Product, ProductDomain, int>
{
    public ProductGenericRepositoryTests()
    {
        _repository = new Repository<Product, ProductDomain, int>(_context, _mapper);
    }

    [Fact]
    public async Task FindProducts_ReturnsCorrectProducts()
    {
        var product = CreateDomainModel();
        await _repository.AddAsync(product);
        await _repository.SaveAsync();

        var foundProducts = await _repository.FindAsync(p => p.Name == product.Name);

        Assert.Contains(foundProducts, p => p.Name == product.Name);
    }

    [Fact]
    public async Task FindProducts_ReturnsEmpty_WhenPredicateDoesNotMatch()
    {
        var foundProducts = await _repository.FindAsync(p => p.Name == "NonExistentName");

        Assert.Empty(foundProducts);
    }
}