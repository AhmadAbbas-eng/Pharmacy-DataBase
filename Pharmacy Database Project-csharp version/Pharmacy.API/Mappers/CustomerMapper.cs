using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class CustomerMapper
{
    public partial CustomerDto ToDto(CustomerDomain domain);
    public partial CustomerDomain ToDomain(CustomerDto dto);
}