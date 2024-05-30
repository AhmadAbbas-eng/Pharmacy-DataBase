using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ISupplierOrderRepository
{
    Task<SupplierOrderDomain> GetByOrderIdAsync(int orderId);
    Task<IEnumerable<SupplierOrderDomain>> GetAllAsync();
    Task<SupplierOrderDomain?> GetByIdAsync(int id);
    Task<SupplierOrderDomain> AddAsync(SupplierOrderDomain supplierOrder);
    Task<SupplierOrderDomain?> UpdateAsync(SupplierOrderDomain supplierOrder);
    Task<bool> DeleteAsync(int id);
}