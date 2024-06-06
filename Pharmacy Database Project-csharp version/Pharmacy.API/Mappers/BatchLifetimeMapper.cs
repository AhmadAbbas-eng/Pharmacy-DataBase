using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class BatchLifetimeMapper
{
    public partial BatchLifetimeDto ToDto(BatchLifetime domain);
    public partial BatchLifetime ToDomain(BatchLifetimeDto dto);
}