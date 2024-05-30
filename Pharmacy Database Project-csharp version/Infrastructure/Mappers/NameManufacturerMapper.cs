using Domain.Models;
using Infrastructure.Models;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public abstract partial class NameManufacturerMapper
{
    public partial NameManufacturerDomain MapToDomain(NameManufacturer nameManufacturer);

    public partial NameManufacturer MapToEntity(NameManufacturerDomain nameManufacturerDomain);

    public partial void MapToEntity(NameManufacturerDomain source, NameManufacturer destination);
}