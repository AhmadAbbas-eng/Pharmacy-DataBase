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
    private readonly IRepository<ProductDomain, int> _productRepository;
    private readonly IMapper _mapper;
    private readonly Fixture _fixture;
    
    public ProductRepositoryTest() : base()
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
    public void AddProduct_AddsToDatabase()
    {

        var domainModel = CreateProductDomain();
        domainModel.ProductId = _productRepository.Add(domainModel);
        _productRepository.Save();
        
        var productDb = Context.Products.FirstOrDefault(p => p.ProductId == domainModel.ProductId);
        Assert.NotNull(productDb);
        var expectedProduct = _mapper.Map<Product>(domainModel);
        productDb.Should().BeEquivalentTo(expectedProduct, options => options.ExcludingMissingMembers());
    }
    
    [Fact]
    public void AddProduct_ReturnsCorrectId()
    {
        var domainModel = CreateProductDomain();

        var addedProductId = _productRepository.Add(domainModel);
        _productRepository.Save();

        Assert.NotEqual(0, addedProductId);
    }
    
    [Fact]
    public void AddProduct_ThrowsException_WhenNull()
    {
        Assert.Throws<ArgumentNullException>(() => _productRepository.Add(null));
    }
    
    [Fact]
    public void GetProductById_ReturnsNull_WhenInvalidId()
    {
        var product = _productRepository.GetById(-1);
        Assert.Null(product);
    }
    
    [Fact]
    public void GetAllProducts_ReturnsAllProducts()
    {
        int numberOfProducts = 5;
        var generatedProducts = new ProductDomain[numberOfProducts];

        for (int i = 0; i < numberOfProducts; i++)
        {
            generatedProducts[i] = CreateProductDomain();
            generatedProducts[i].ProductId = _productRepository.Add(generatedProducts[i]);
        }

        _productRepository.Save();

        var products = _productRepository.GetAll();

        Assert.NotEmpty(products);
        Assert.Equal(numberOfProducts, products.Count());
        generatedProducts.Should().BeEquivalentTo(products, options => options.ExcludingMissingMembers());
    }
    
    [Fact]
    public void GetAllProducts_ReturnsEmpty_WhenNoProducts()
    {
        var products = _productRepository.GetAll();

        Assert.Empty(products);
    }
    
    [Fact]
    public void UpdateProduct_UpdatesExistingProduct()
    {
        var domainModel = CreateProductDomain();
        var addedProductId = _productRepository.Add(domainModel);
        _productRepository.Save();

        var updatedProduct = _productRepository.GetById(addedProductId);
        var newName = "New Name";
        updatedProduct.Name = newName;

        _productRepository.Update(updatedProduct);
        _productRepository.Save();

        var productInDb = _productRepository.GetById(addedProductId);

        updatedProduct.Should().BeEquivalentTo(productInDb, options => options.ExcludingMissingMembers());
    }
    
    [Fact]
    public void UpdateProduct_UpdatesShouldThroughException()
    {
        Assert.Throws<ArgumentException>(() =>
        {
            var updatedProduct = CreateProductDomain();
            var newName = "New Name";
            updatedProduct.Name = newName;

            _productRepository.Update(updatedProduct);
            _productRepository.Save();
        });
    }
    
    [Fact]
    public void DeleteProduct_RemovesProduct()
    {
        var product = _fixture.Create<ProductDomain>();
        var addedProduct = _productRepository.Add(product);
        _productRepository.Save();

        _productRepository.Delete(product);
        _productRepository.Save();

        var foundProduct = _productRepository.GetById(addedProduct);
        Assert.Null(foundProduct);
    }
    
    [Fact]
    public void DeleteProduct_ThrowsException_WhenProductDoesNotExist()
    {
        var product = _fixture.Build<ProductDomain>()
            .Without(p => p.ProductId) 
            .Create();

        Assert.Throws<ArgumentException>(() => _productRepository.Delete(product));
    }
    
    [Fact]
    public void FindProducts_ReturnsCorrectProducts()
    {
        var product = _fixture.Create<ProductDomain>();
        _productRepository.Add(product);
        _productRepository.Save();

        var foundProducts = _productRepository.Find(p => p.Name == product.Name);

        Assert.Contains(foundProducts, p => p.Name == product.Name);
    }
    
    [Fact]
    public void FindProducts_ReturnsEmpty_WhenPredicateDoesNotMatch()
    {
        var foundProducts = _productRepository.Find(p => p.Name == "NonExistentName");

        Assert.Empty(foundProducts);
    }
}
