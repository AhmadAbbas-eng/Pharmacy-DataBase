using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ISupplierRepository
{
    Task<IEnumerable<SupplierDomain>> ListSuppliersWithDueAmountsAsync();
    Task<IEnumerable<SupplierDomain>> FindSuppliersByProductAsync(string productName);
}