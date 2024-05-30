using Domain.Models;
using Infrastructure.Models;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public abstract partial class TaxMapper
{
    public partial TaxDomain MapToDomain(Tax tax);

    public partial Tax MapToEntity(TaxDomain taxDomain);

    public partial void MapToEntity(TaxDomain source, Tax destination);
}