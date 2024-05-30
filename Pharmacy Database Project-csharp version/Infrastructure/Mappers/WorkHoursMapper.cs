using Domain.Models;
using Infrastructure.Entities;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public partial class WorkHoursMapper
{
    public partial WorkHoursDomain MapToDomain(WorkHours workHours);

    public partial WorkHours MapToEntity(WorkHoursDomain workHoursDomain);

    public partial void MapToEntity(WorkHoursDomain source, WorkHours destination);
}
