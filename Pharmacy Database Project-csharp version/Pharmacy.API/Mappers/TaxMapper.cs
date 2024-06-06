using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class TaxMapper
{
    public partial TaxDto ToDto(TaxDomain domain);
    public partial TaxDomain ToDomain(TaxDto dto);
}