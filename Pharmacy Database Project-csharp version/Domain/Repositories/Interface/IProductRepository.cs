using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IProductRepository
{
    Task<int> GetTotalAmountByIdAsync(int id);
    Task<List<ProductDomain>> GetOutOfStockAsync();
    Task<double> CalculateAveragePriceAsync();
}