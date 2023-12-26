using Domain.Models;

namespace Domain.Repositories.Interface;

public interface IDrugRepository : IRepository<DrugDomain, int>
{
    Task<IEnumerable<DrugDomain>> FindByRiskCategoryAsync(char riskCategory);
}