using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class ProductMapper
{
    public partial ProductDto ToDto(ProductDomain domain);
    public partial ProductDomain ToDomain(ProductDto dto);
}