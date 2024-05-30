using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ITaxRepository
{
    Task<ICollection<TaxDomain>> FindByStartAndEndDateAsync(DateTime startDate, DateTime endDate);
    Task<IEnumerable<TaxDomain>> GetAllAsync();
    Task<TaxDomain?> GetByIdAsync(int id);
    Task<TaxDomain> AddAsync(TaxDomain tax);
    Task<TaxDomain?> UpdateAsync(TaxDomain tax);
    Task<bool> DeleteAsync(int id);
}