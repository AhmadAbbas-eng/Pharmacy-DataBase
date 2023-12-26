using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ISupplierRepository : IRepository<SupplierDomain, int>
{
    Task<IEnumerable<SupplierDomain>> ListSuppliersWithDueAmountsAsync();
    Task<IEnumerable<SupplierDomain>> FindByProductAsync(string productName);
}