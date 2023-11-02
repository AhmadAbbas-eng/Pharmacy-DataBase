using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IDrugRepository
{
    Task<IEnumerable<DrugDomain>> FindDrugsByRiskCategoryAsync(char riskCategory);
}