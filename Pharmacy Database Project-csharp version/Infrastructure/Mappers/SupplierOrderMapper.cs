using Domain.Models;
using Infrastructure.Models;
using Riok.Mapperly.Abstractions;

namespace Infrastructure.Mappers;

[Mapper]
public abstract partial class SupplierOrderMapper
{
    public partial SupplierOrderDomain MapToDomain(SupplierOrder supplierOrder);

    public partial SupplierOrder MapToEntity(SupplierOrderDomain supplierOrderDomain);

    public partial void MapToEntity(SupplierOrderDomain source, SupplierOrder destination);
}