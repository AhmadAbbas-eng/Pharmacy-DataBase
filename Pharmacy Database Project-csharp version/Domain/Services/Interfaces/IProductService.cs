using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IProductService
{
    Task<int> GetTotalAmountByIdAsync(int productId);
    Task<List<ProductDomain>> GetOutOfStockAsync();
    Task<double> CalculateAveragePriceAsync();
    Task<IEnumerable<ProductDomain>> GetAllProductsAsync();
    Task<ProductDomain?> GetProductByIdAsync(int id);
    Task<ProductDomain> AddProductAsync(ProductDomain product);
    Task<ProductDomain?> UpdateProductAsync(ProductDomain product);
    Task<bool> DeleteProductAsync(int id);
}