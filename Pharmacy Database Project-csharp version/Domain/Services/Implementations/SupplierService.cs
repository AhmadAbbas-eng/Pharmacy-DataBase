using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class SupplierService : ISupplierService
{
    private readonly ISupplierRepository _supplierRepository;

    public SupplierService(ISupplierRepository supplierRepository)
    {
        _supplierRepository = supplierRepository;
    }

    public async Task<IEnumerable<SupplierDomain>> ListSuppliersWithDueAmountsAsync()
    {
        return await _supplierRepository.ListSuppliersWithDueAmountsAsync();
    }

    public async Task<IEnumerable<SupplierDomain>> FindSuppliersByProductAsync(string productName)
    {
        return await _supplierRepository.FindByProductAsync(productName);
    }

    public async Task<IEnumerable<SupplierDomain>> GetAllSuppliersAsync()
    {
        return await _supplierRepository.GetAllAsync();
    }

    public async Task<SupplierDomain?> GetSupplierByIdAsync(int id)
    {
        return await _supplierRepository.GetByIdAsync(id);
    }

    public async Task<SupplierDomain> AddSupplierAsync(SupplierDomain supplier)
    {
        return await _supplierRepository.AddAsync(supplier);
    }

    public async Task<SupplierDomain?> UpdateSupplierAsync(SupplierDomain supplier)
    {
        return await _supplierRepository.UpdateAsync(supplier);
    }

    public async Task<bool> DeleteSupplierAsync(int id)
    {
        return await _supplierRepository.DeleteAsync(id);
    }
}