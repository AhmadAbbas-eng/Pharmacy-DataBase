using Domain.Models;
using Infrastructure.Entities;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public partial class DrugMapper
{
    public partial DrugDomain MapToDomain(Drug drug);

    public partial Drug MapToEntity(DrugDomain drugDomain);

    public partial void MapToEntity(DrugDomain source, Drug destination);
}
