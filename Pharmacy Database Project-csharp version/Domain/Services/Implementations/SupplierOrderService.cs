using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class SupplierOrderService : ISupplierOrderService
{
    private readonly IRepository<SupplierOrderDomain, int> _orderRepository;

    public SupplierOrderService(IRepository<SupplierOrderDomain, int> orderRepository)
    {
        _orderRepository = orderRepository;
    }

    public float CalculateOrderTotalAfterDiscount(int orderId)
    {
        var order = _orderRepository.GetById(orderId);
        if (order == null) throw new ArgumentException("Order not found.");

        var discountAmount = order.OrderCost * (order.OrderDiscount / 100);
        return order.OrderCost - discountAmount;
    }
}