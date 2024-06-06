using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class PaymentMapper
{
    public partial PaymentDto ToDto(PaymentDomain domain);
    public partial PaymentDomain ToDomain(PaymentDto dto);
}