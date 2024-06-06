using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class PendingPaymentMapper
{
    public partial PendingPaymentDto ToDto(PendingPayment domain);
    public partial PendingPayment ToDomain(PendingPaymentDto dto);
}