using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IDrugRepository
{
    Task<IEnumerable<DrugDomain>> FindByRiskCategoryAsync(char riskCategory);
    Task<IEnumerable<DrugDomain>> GetAllAsync();
    Task<DrugDomain?> GetByIdAsync(int id);
    Task<DrugDomain> AddAsync(DrugDomain drug);
    Task<DrugDomain?> UpdateAsync(DrugDomain drug);
    Task<bool> DeleteAsync(int id);
}