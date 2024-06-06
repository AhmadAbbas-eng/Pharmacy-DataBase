using Domain.Models;

namespace Domain.Services.Interfaces;

public interface ITaxService
{
    Task<double> CalculateTaxForPeriodAsync(DateTime startDate, DateTime endDate);
    Task<ICollection<TaxDomain>> GetTaxesByStartAndEndDateAsync(DateTime startDate, DateTime endDate);
    Task<IEnumerable<TaxDomain>> GetAllTaxesAsync();
    Task<TaxDomain?> GetTaxByIdAsync(int id);
    Task<TaxDomain> AddTaxAsync(TaxDomain tax);
    Task<TaxDomain?> UpdateTaxAsync(TaxDomain tax);
    Task<bool> DeleteTaxAsync(int id);
}