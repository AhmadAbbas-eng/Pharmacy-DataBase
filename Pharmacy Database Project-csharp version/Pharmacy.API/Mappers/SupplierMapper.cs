using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class SupplierMapper
{
    public partial SupplierDto ToDto(SupplierDomain domain);
    public partial SupplierDomain ToDomain(SupplierDto dto);
}