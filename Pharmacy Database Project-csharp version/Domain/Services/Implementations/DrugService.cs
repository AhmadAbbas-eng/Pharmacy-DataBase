using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class DrugService : IDrugService
{
    private readonly IDrugRepository _drugRepository;

    public DrugService(IDrugRepository drugRepository)
    {
        _drugRepository = drugRepository;
    }

    public async Task<IEnumerable<DrugDomain>> GetDrugsByRiskCategoryAsync(char riskCategory)
    {
        return await _drugRepository.FindByRiskCategoryAsync(riskCategory);
    }

    public async Task<IEnumerable<DrugDomain>> GetAllDrugsAsync()
    {
        return await _drugRepository.GetAllAsync();
    }

    public async Task<DrugDomain?> GetDrugByIdAsync(int id)
    {
        return await _drugRepository.GetByIdAsync(id);
    }

    public async Task<DrugDomain> AddDrugAsync(DrugDomain drug)
    {
        return await _drugRepository.AddAsync(drug);
    }

    public async Task<DrugDomain?> UpdateDrugAsync(DrugDomain drug)
    {
        return await _drugRepository.UpdateAsync(drug);
    }

    public async Task<bool> DeleteDrugAsync(int id)
    {
        return await _drugRepository.DeleteAsync(id);
    }
}