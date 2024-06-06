using Domain.Models;

namespace Domain.Services.Interfaces;

public interface IDrugService
{
    Task<IEnumerable<DrugDomain>> GetDrugsByRiskCategoryAsync(char riskCategory);
    Task<IEnumerable<DrugDomain>> GetAllDrugsAsync();
    Task<DrugDomain?> GetDrugByIdAsync(int id);
    Task<DrugDomain> AddDrugAsync(DrugDomain drug);
    Task<DrugDomain?> UpdateDrugAsync(DrugDomain drug);
    Task<bool> DeleteDrugAsync(int id);
}