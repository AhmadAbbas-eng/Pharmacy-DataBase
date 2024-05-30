using Riok.Mapperly.Abstractions;
using Domain.Models;
using Infrastructure.Entities;

namespace Infrastructure.Mappers;

[Mapper]
public partial class BatchMapper
{
    public partial BatchDomain MapToDomain(Batch batch);

    public partial Batch MapToEntity(BatchDomain batchDomain);

    public partial void MapToEntity(BatchDomain source, Batch destination);
}
