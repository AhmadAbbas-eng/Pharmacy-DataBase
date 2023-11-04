namespace Domain.Services.Interfaces;

public interface ISupplierOrderService
{
    float CalculateOrderTotalAfterDiscount(int orderId);
}