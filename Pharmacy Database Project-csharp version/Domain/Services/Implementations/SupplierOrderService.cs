using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class SupplierOrderService : ISupplierOrderService
{
    private readonly ISupplierOrderRepository _supplierOrderRepository;

    public SupplierOrderService(ISupplierOrderRepository orderRepository)
    {
        _supplierOrderRepository = orderRepository;
    }

    public async Task<SupplierOrderDomain> GetOrderByOrderIdAsync(int orderId)
    {
        return await _supplierOrderRepository.GetByOrderIdAsync(orderId);
    }

    public async Task<IEnumerable<SupplierOrderDomain>> GetAllSupplierOrdersAsync()
    {
        return await _supplierOrderRepository.GetAllAsync();
    }

    public async Task<SupplierOrderDomain?> GetSupplierOrderByIdAsync(int id)
    {
        return await _supplierOrderRepository.GetByIdAsync(id);
    }

    public async Task<SupplierOrderDomain> AddSupplierOrderAsync(SupplierOrderDomain supplierOrder)
    {
        return await _supplierOrderRepository.AddAsync(supplierOrder);
    }

    public async Task<SupplierOrderDomain?> UpdateSupplierOrderAsync(SupplierOrderDomain supplierOrder)
    {
        return await _supplierOrderRepository.UpdateAsync(supplierOrder);
    }

    public async Task<bool> DeleteSupplierOrderAsync(int id)
    {
        return await _supplierOrderRepository.DeleteAsync(id);
    }

    public async Task<float> CalculateOrderTotalAfterDiscount(int orderId)
    {
        var order = await _supplierOrderRepository.GetByOrderIdAsync(orderId);
        if (order is null) throw new ArgumentException("Order not found.");

        var discountAmount = order.OrderCost * (order.OrderDiscount / 100);
        var result = order.OrderCost - discountAmount;
        return result;
    }
}