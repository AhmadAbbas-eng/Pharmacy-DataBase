using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class SupplierOrderService : ISupplierOrderService
{
    private readonly ISupplierOrderRepository _orderRepository;

    public SupplierOrderService(ISupplierOrderRepository orderRepository)
    {
        _orderRepository = orderRepository;
    }

    public async Task<float> CalculateOrderTotalAfterDiscount(int orderId)
    {
        var order = await _orderRepository.GetByIdAsync(orderId);
        if (order is null) throw new ArgumentException("Order not found.");

        var discountAmount = order.OrderCost * (order.OrderDiscount / 100);
        var result = order.OrderCost - discountAmount;
        return result;
    }
}