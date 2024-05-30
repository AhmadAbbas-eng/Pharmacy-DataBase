using Domain.Models;
using Infrastructure.Models;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public partial class SupplierMapper
{
    public partial SupplierDomain MapToDomain(Supplier supplier);

    public partial Supplier MapToEntity(SupplierDomain supplierDomain);

    public partial void MapToEntity(SupplierDomain source, Supplier destination);
}