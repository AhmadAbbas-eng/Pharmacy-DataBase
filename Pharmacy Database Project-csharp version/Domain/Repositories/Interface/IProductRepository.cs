using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IProductRepository
{
    Task<int> GetTotalAmountByIdAsync(int productId);
    Task<List<ProductDomain>> GetOutOfStockAsync();
    Task<double> CalculateAveragePriceAsync();
    Task<IEnumerable<ProductDomain>> GetAllAsync();
    Task<ProductDomain?> GetByIdAsync(int id);
    Task<ProductDomain> AddAsync(ProductDomain product);
    Task<ProductDomain?> UpdateAsync(ProductDomain product);
    Task<bool> DeleteAsync(int id);
}