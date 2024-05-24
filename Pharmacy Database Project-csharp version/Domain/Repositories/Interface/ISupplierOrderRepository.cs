using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ISupplierOrderRepository
{
    Task<SupplierOrderDomain> GetByOrderIdAsync(int orderId);
}