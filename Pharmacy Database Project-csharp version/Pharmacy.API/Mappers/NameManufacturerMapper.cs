using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class NameManufacturerMapper
{
    public partial NameManufacturerDto ToDto(NameManufacturerDomain domain);
    public partial NameManufacturerDomain ToDomain(NameManufacturerDto dto);
}