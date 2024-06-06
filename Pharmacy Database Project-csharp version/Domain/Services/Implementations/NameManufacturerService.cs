using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Interfaces;

namespace Domain.Services.Implementations;

public class NameManufacturerService : INameManufacturerService
{
    private readonly INameManufacturerRepository _nameManufacturerRepository;

    public NameManufacturerService(INameManufacturerRepository nameManufacturerRepository)
    {
        _nameManufacturerRepository = nameManufacturerRepository;
    }

    public async Task<IEnumerable<string>> GetDistinctNamesAsync()
    {
        return await _nameManufacturerRepository.GetDistinctAsync();
    }

    public async Task UpdateNameAsync(string oldName, string newName)
    {
        await _nameManufacturerRepository.UpdateAsync(oldName, newName);
    }

    public async Task<IEnumerable<NameManufacturerDomain>> GetAllNameManufacturersAsync()
    {
        return await _nameManufacturerRepository.GetAllAsync();
    }

    public async Task<NameManufacturerDomain?> GetNameManufacturerByIdAsync(int id)
    {
        return await _nameManufacturerRepository.GetByIdAsync(id);
    }

    public async Task<NameManufacturerDomain> AddNameManufacturerAsync(NameManufacturerDomain nameManufacturer)
    {
        return await _nameManufacturerRepository.AddAsync(nameManufacturer);
    }

    public async Task<NameManufacturerDomain?> UpdateNameManufacturerAsync(NameManufacturerDomain nameManufacturer)
    {
        return await _nameManufacturerRepository.UpdateAsync(nameManufacturer);
    }

    public async Task<bool> DeleteNameManufacturerAsync(int id)
    {
        return await _nameManufacturerRepository.DeleteAsync(id);
    }
}