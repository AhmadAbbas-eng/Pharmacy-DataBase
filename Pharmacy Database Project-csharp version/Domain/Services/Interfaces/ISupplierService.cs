using Domain.Models;

namespace Domain.Services.Interfaces;

public interface ISupplierService
{
    Task<IEnumerable<SupplierDomain>> ListSuppliersWithDueAmountsAsync();
    Task<IEnumerable<SupplierDomain>> FindSuppliersByProductAsync(string productName);
    Task<IEnumerable<SupplierDomain>> GetAllSuppliersAsync();
    Task<SupplierDomain?> GetSupplierByIdAsync(int id);
    Task<SupplierDomain> AddSupplierAsync(SupplierDomain supplier);
    Task<SupplierDomain?> UpdateSupplierAsync(SupplierDomain supplier);
    Task<bool> DeleteSupplierAsync(int id);
}