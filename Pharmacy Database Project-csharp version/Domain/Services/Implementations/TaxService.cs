using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class TaxService : ITaxService
{
    private readonly ITaxRepository _taxRepository;

    public TaxService(ITaxRepository taxRepository)
    {
        _taxRepository = taxRepository;
    }

    public async Task<double> CalculateTaxForPeriodAsync(DateTime startDate, DateTime endDate)
    {
        var taxes = await _taxRepository.FindByStartAndEndDateAsync(startDate, endDate);
        return taxes.Sum(x => x.TaxValue);
    }
}