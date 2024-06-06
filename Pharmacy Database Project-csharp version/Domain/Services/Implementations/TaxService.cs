using Domain.Models;
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

    public async Task<ICollection<TaxDomain>> GetTaxesByStartAndEndDateAsync(DateTime startDate, DateTime endDate)
    {
        return await _taxRepository.FindByStartAndEndDateAsync(startDate, endDate);
    }

    public async Task<IEnumerable<TaxDomain>> GetAllTaxesAsync()
    {
        return await _taxRepository.GetAllAsync();
    }

    public async Task<TaxDomain?> GetTaxByIdAsync(int id)
    {
        return await _taxRepository.GetByIdAsync(id);
    }

    public async Task<TaxDomain> AddTaxAsync(TaxDomain tax)
    {
        return await _taxRepository.AddAsync(tax);
    }

    public async Task<TaxDomain?> UpdateTaxAsync(TaxDomain tax)
    {
        return await _taxRepository.UpdateAsync(tax);
    }

    public async Task<bool> DeleteTaxAsync(int id)
    {
        return await _taxRepository.DeleteAsync(id);
    }

    public async Task<double> CalculateTaxForPeriodAsync(DateTime startDate, DateTime endDate)
    {
        var taxes = await _taxRepository.FindByStartAndEndDateAsync(startDate, endDate);
        return taxes.Sum(x => x.TaxValue);
    }
}