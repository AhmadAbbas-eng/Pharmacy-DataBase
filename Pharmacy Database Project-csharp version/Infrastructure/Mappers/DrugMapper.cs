using Domain.Models;
using Infrastructure.Models;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public abstract partial class DrugMapper
{
    public partial DrugDomain MapToDomain(Drug drug);

    public partial Drug MapToEntity(DrugDomain drugDomain);

    public partial void MapToEntity(DrugDomain source, Drug destination);
}