using Domain.Models;
using Infrastructure.Entities;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public partial class TaxMapper
{
    public partial TaxDomain MapToDomain(Tax tax);

    public partial Tax MapToEntity(TaxDomain taxDomain);

    public partial void MapToEntity(TaxDomain source, Tax destination);
}