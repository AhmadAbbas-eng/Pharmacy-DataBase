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
    public void FindProducts_ReturnsCorrectProducts()
    {
        var product = CreateDomainModel();
        _repository.Add(product);
        _repository.Save();

        var foundProducts = _repository.Find(p => p.Name == product.Name);

        Assert.Contains(foundProducts, p => p.Name == product.Name);
    }
    
    [Fact]
    public void FindProducts_ReturnsEmpty_WhenPredicateDoesNotMatch()
    {
        var foundProducts = _repository.Find(p => p.Name == "NonExistentName");

        Assert.Empty(foundProducts);
    }
}