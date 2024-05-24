using Domain.Models;

namespace Domain.Repositories.Interface;

public interface ITaxRepository
{
    Task<ICollection<TaxDomain>> FindByStartAndEndDateAsync(DateTime startDate, DateTime endDate);
}