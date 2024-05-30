using Domain.Models;
using Infrastructure.Entities;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public partial class EmployeeMapper
{
    public partial EmployeeDomain MapToDomain(Employee employee);

    public partial Employee MapToEntity(EmployeeDomain employeeDomain);

    public partial void MapToEntity(EmployeeDomain source, Employee destination);
}