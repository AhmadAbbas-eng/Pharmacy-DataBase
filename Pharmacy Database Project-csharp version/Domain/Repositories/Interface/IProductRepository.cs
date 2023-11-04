using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IProductRepository
{
    Task<int> GetTotalAmountByProductIdAsync(int productId);
    Task<List<ProductDomain>> GetOutOfStockProductsAsync();
    Task<double> CalculateAverageProductPriceAsync();
}