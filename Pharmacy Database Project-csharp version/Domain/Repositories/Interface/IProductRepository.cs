using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IProductRepository
{
    Task<int> GetTotalAmountByProductIdAsync(int productId);
    Task<List<ProductDomian>> GetOutOfStockProductsAsync();
    Task<double> CalculateAverageProductPriceAsync();
}
