using Domain.Models;
using Infrastructure.Models;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public partial class CustomerMapper
{
    public partial CustomerDomain MapToDomain(Customer customer);

    public partial Customer MapToEntity(CustomerDomain customerDomain);

    public partial void MapToEntity(CustomerDomain source, Customer destination);
}