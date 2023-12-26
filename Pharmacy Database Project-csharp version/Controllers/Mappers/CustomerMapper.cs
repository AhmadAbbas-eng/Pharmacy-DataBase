using Controllers.Model.Dto;
using Domain.Models;
using Riok.Mapperly.Abstractions;

namespace Controllers.Mappers;

[Mapper]
public partial class CustomerMapper
{
    public partial CustomerDto CustomerToDto(CustomerDomain customerDomain);
    public partial IEnumerable<CustomerDto> CustomerToDto(IEnumerable<CustomerDomain> customerDomain);
    public partial CustomerDomain CustomerToDomain(CustomerDto customerDto);
    
    public partial IEnumerable<CustomerDomain> CustomerToDomain(IEnumerable<CustomerDto> customerDto);
}