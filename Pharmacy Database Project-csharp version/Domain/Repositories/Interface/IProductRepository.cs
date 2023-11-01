using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IProductRepository
{
    int GetTotalAmountByProductId(int productId);
    List<ProductDomian> GetOutOfStockProducts();
    Task<double> CalculateAverageProductPriceAsync();
}
