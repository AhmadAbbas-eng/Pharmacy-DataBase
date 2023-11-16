namespace Domain.Services.Interfaces;

public interface ISupplierOrderService
{
    Task<float> CalculateOrderTotalAfterDiscount(int orderId);
}