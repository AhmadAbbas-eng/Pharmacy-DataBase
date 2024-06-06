using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class BatchMapper
{
    public partial BatchDto ToDto(BatchDomain domain);
    public partial BatchDomain ToDomain(BatchDto dto);
}