using Domain.Models;
using Infrastructure.Models;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public abstract partial class BatchMapper
{
    public partial BatchDomain MapToDomain(Batch batch);

    public partial Batch MapToEntity(BatchDomain batchDomain);

    public partial void MapToEntity(BatchDomain source, Batch destination);
}