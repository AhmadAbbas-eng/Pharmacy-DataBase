using Domain.Models;

namespace Domain.Services.Interfaces;

public interface ISupplierOrderService
{
    Task<float> CalculateOrderTotalAfterDiscount(int orderId);
    Task<SupplierOrderDomain> GetOrderByOrderIdAsync(int orderId);
    Task<IEnumerable<SupplierOrderDomain>> GetAllSupplierOrdersAsync();
    Task<SupplierOrderDomain?> GetSupplierOrderByIdAsync(int id);
    Task<SupplierOrderDomain> AddSupplierOrderAsync(SupplierOrderDomain supplierOrder);
    Task<SupplierOrderDomain?> UpdateSupplierOrderAsync(SupplierOrderDomain supplierOrder);
    Task<bool> DeleteSupplierOrderAsync(int id);
}