using Domain.Models;
using Infrastructure.Models;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public abstract partial class WorkHoursMapper
{
    public partial WorkHoursDomain MapToDomain(WorkHours workHours);

    public partial WorkHours MapToEntity(WorkHoursDomain workHoursDomain);

    public partial void MapToEntity(WorkHoursDomain source, WorkHours destination);
}