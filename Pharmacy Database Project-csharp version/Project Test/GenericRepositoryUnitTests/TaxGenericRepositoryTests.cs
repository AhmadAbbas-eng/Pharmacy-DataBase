using Domain.Models;
using Infrastructure;
using Infrastructure.Entities;
using Infrastructure.Repository;

namespace Project_Test.GenericRepositoryUnitTests;

public class TaxGenericRepositoryTests: BaseGenericRepositoryTests<PharmacyDbContext, Tax, TaxDomain, string>
{
    public TaxGenericRepositoryTests()
    {
        _repository = new Repository<Tax, TaxDomain, string>(_context, _mapper);
    }
}
