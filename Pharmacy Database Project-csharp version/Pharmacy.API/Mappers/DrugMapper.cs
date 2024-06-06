using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class DrugMapper
{
    public partial DrugDto ToDto(DrugDomain domain);
    public partial DrugDomain ToDomain(DrugDto dto);
}