using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ISupplierRepository
{
    Task<IEnumerable<SupplierDomain>> ListSuppliersWithDueAmountsAsync();
    Task<IEnumerable<SupplierDomain>> FindByProductAsync(string productName);
    Task<IEnumerable<SupplierDomain>> GetAllAsync();
    Task<SupplierDomain?> GetByIdAsync(int id);
    Task<SupplierDomain> AddAsync(SupplierDomain supplier);
    Task<SupplierDomain?> UpdateAsync(SupplierDomain supplier);
    Task<bool> DeleteAsync(int id);
}