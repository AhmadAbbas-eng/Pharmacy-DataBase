using Domain.Models;
using Infrastructure.Entities;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public partial class ProductMapper
{
    public partial ProductDomain MapToDomain(Product product);

    public partial Product MapToEntity(ProductDomain productDomain);

    public partial void MapToEntity(ProductDomain source, Product destination);
}