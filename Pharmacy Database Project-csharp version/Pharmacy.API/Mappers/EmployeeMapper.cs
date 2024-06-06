using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class EmployeeMapper
{
    public partial EmployeeDto ToDto(EmployeeDomain domain);
    public partial EmployeeDomain ToDomain(EmployeeDto dto);
}