using AutoFixture;
using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using FluentAssertions;
using Infrastructure;
using Infrastructure.Entities;
using Infrastructure.Repository;

namespace Project_Test;

public class ProductRepositoryTest : BaseTest<PharmacyDbContext>
{
    private readonly Fixture _fixture;
    private readonly IMapper _mapper;
    private readonly IRepository<ProductDomain, int> _productRepository;

    public ProductRepositoryTest()
    {
        var mockMapperConfig = new MapperConfiguration(cfg =>
        {
            cfg.CreateMap<ProductDomain, Product>();
            cfg.CreateMap<Product, ProductDomain>();
        });
        _mapper = mockMapperConfig.CreateMapper();

        _productRepository = new Repository<Product, ProductDomain, int>(Context, _mapper);

        _fixture = new Fixture();
    }


    private ProductDomain CreateProductDomain()
    {
        return _fixture.Build<ProductDomain>()
            .Without(p => p.ProductId)
            .Create();
    }

    [Fact]
    public async Task AddProduct_AddsToDatabase()
    {
        var domainModel = CreateProductDomain();
        domainModel.ProductId = await _productRepository.AddAsync(domainModel);
        await _productRepository.SaveAsync();

        var productDb = Context.Products.FirstOrDefault(p => p.ProductId == domainModel.ProductId);
        Assert.NotNull(productDb);
        var expectedProduct = _mapper.Map<Product>(domainModel);
        productDb.Should().BeEquivalentTo(expectedProduct, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public async Task AddProduct_ReturnsCorrectId()
    {
        var domainModel = CreateProductDomain();

        var addedProductId = await _productRepository.AddAsync(domainModel);
        await _productRepository.SaveAsync();

        Assert.NotEqual(0, addedProductId);
    }

    [Fact]
    public async Task AddProduct_ThrowsException_WhenNull()
    {
        await Assert.ThrowsAsync<ArgumentNullException>(() => _productRepository.AddAsync(null));
    }

    [Fact]
    public async Task GetProductById_ReturnsNull_WhenInvalidId()
    {
        var product = await _productRepository.GetByIdAsync(-1);
        Assert.Null(product);
    }

    [Fact]
    public async Task GetAllProducts_ReturnsAllProducts()
    {
        var numberOfProducts = 5;
        var generatedProducts = new ProductDomain[numberOfProducts];

        for (var i = 0; i < numberOfProducts; i++)
        {
            generatedProducts[i] = CreateProductDomain();
            generatedProducts[i].ProductId = await _productRepository.AddAsync(generatedProducts[i]);
        }

        await _productRepository.SaveAsync();

        var products = await _productRepository.GetAllAsync();

        Assert.NotEmpty(products);
        Assert.Equal(numberOfProducts, products.Count());
        generatedProducts.Should().BeEquivalentTo(products, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public async Task GetAllProducts_ReturnsEmpty_WhenNoProducts()
    {
        var products = await _productRepository.GetAllAsync();

        Assert.Empty(products);
    }

    [Fact]
    public async Task UpdateProduct_UpdatesExistingProduct()
    {
        var domainModel = CreateProductDomain();
        var addedProductId = await _productRepository.AddAsync(domainModel);
        await _productRepository.SaveAsync();

        var updatedProduct = await _productRepository.GetByIdAsync(addedProductId);
        var newName = "New Name";
        updatedProduct.Name = newName;

        _productRepository.Update(updatedProduct);
        await _productRepository.SaveAsync();

        var productInDb = await _productRepository.GetByIdAsync(addedProductId);

        updatedProduct.Should().BeEquivalentTo(productInDb, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public async Task UpdateProduct_UpdatesShouldThroughException()
    {
        await Assert.ThrowsAsync<ArgumentException>(() =>
        {
            var updatedProduct = CreateProductDomain();
            var newName = "New Name";
            updatedProduct.Name = newName;

            _productRepository.Update(updatedProduct);
            return _productRepository.SaveAsync();
        });
    }

    [Fact]
    public async Task DeleteProduct_RemovesProduct()
    {
        var product = _fixture.Create<ProductDomain>();
        var addedProduct = await _productRepository.AddAsync(product);
        await _productRepository.SaveAsync();

        await _productRepository.DeleteAsync(addedProduct);
        await _productRepository.SaveAsync();

        var foundProduct = await _productRepository.GetByIdAsync(addedProduct);
        Assert.Null(foundProduct);
    }

    [Fact]
    public async Task DeleteProduct_ThrowsException_WhenProductDoesNotExist()
    {
        var product = _fixture.Build<ProductDomain>()
            .Without(p => p.ProductId)
            .Create();

        await Assert.ThrowsAsync<ArgumentNullException>(() => _productRepository.DeleteAsync(product.ProductId));
    }

    [Fact]
    public async Task FindProducts_ReturnsCorrectProducts()
    {
        var product = _fixture.Create<ProductDomain>();
        await _productRepository.AddAsync(product);
        await _productRepository.SaveAsync();

        var foundProducts = await _productRepository.FindAsync(p => p.Name == product.Name);

        Assert.Contains(foundProducts, p => p.Name == product.Name);
    }

    [Fact]
    public async Task FindProducts_ReturnsEmpty_WhenPredicateDoesNotMatch()
    {
        var foundProducts = await _productRepository.FindAsync(p => p.Name == "NonExistentName");

        Assert.Empty(foundProducts);
    }
}