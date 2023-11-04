using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class TaxService : ITaxService
{
    private readonly IRepository<TaxDomain, string> _taxRepository;

    public TaxService(IRepository<TaxDomain, string> taxRepository)
    {
        _taxRepository = taxRepository;
    }

    public async Task<double> CalculateTaxForPeriodAsync(DateTime startDate, DateTime endDate)
    {
        var taxes = await _taxRepository.FindAsync(t => t.TaxDate >= startDate && t.TaxDate <= endDate);
        return taxes.Sum(t => t.TaxValue);
    }
}
