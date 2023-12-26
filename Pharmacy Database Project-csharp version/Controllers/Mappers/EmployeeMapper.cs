using Controllers.Model.Dto;
using Domain.Models;
using Riok.Mapperly.Abstractions;

namespace Controllers.Mappers;

[Mapper(UseDeepCloning = true)]
public partial class EmployeeMapper 
{
    public partial EmployeeDto EmployeeToDto(EmployeeDomain employeeDomain);
    public partial IEnumerable<EmployeeDto> EmployeeToDto(IEnumerable<EmployeeDomain> employeeDomain);
    public partial EmployeeDomain EmployeeToDomain(EmployeeDto employeeDto);
    
    public partial IEnumerable<EmployeeDomain> EmployeeToDomain(IEnumerable<EmployeeDto> employeeDto);
}