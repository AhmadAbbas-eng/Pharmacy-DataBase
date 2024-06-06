using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class ProductService : IProductService
{
    private readonly IProductRepository _productRepository;

    public ProductService(IProductRepository productRepository)
    {
        _productRepository = productRepository;
    }

    public async Task<int> GetTotalAmountByIdAsync(int productId)
    {
        return await _productRepository.GetTotalAmountByIdAsync(productId);
    }

    public async Task<List<ProductDomain>> GetOutOfStockAsync()
    {
        return await _productRepository.GetOutOfStockAsync();
    }

    public async Task<double> CalculateAveragePriceAsync()
    {
        return await _productRepository.CalculateAveragePriceAsync();
    }

    public async Task<IEnumerable<ProductDomain>> GetAllProductsAsync()
    {
        return await _productRepository.GetAllAsync();
    }

    public async Task<ProductDomain?> GetProductByIdAsync(int id)
    {
        return await _productRepository.GetByIdAsync(id);
    }

    public async Task<ProductDomain> AddProductAsync(ProductDomain product)
    {
        return await _productRepository.AddAsync(product);
    }

    public async Task<ProductDomain?> UpdateProductAsync(ProductDomain product)
    {
        return await _productRepository.UpdateAsync(product);
    }

    public async Task<bool> DeleteProductAsync(int id)
    {
        return await _productRepository.DeleteAsync(id);
    }
}