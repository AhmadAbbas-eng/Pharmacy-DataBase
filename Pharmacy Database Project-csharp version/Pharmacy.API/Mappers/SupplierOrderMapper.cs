using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class SupplierOrderMapper
{
    public partial SupplierOrderDto ToDto(SupplierOrderDomain domain);
    public partial SupplierOrderDomain ToDomain(SupplierOrderDto dto);
}