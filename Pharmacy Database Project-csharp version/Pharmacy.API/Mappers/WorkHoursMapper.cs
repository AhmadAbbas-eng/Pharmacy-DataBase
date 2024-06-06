using Domain.Models;
using Pharmacy.API.Dtos;
using Riok.Mapperly.Abstractions;

namespace Pharmacy.API.Mappers;

[Mapper]
public partial class WorkHoursMapper
{
    public partial WorkHoursDto ToDto(WorkHoursDomain domain);
    public partial WorkHoursDomain ToDomain(WorkHoursDto dto);
}