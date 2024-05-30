using Domain.Models;

namespace Domain.Repositories.Interface;

public interface INameManufacturerRepository
{
    Task<IEnumerable<string>> GetDistinctAsync();
    Task UpdateAsync(string oldName, string newName);
    Task<IEnumerable<NameManufacturerDomain>> GetAllAsync();
    Task<NameManufacturerDomain?> GetByIdAsync(int id);
    Task<NameManufacturerDomain> AddAsync(NameManufacturerDomain nameManufacturer);
    Task<NameManufacturerDomain?> UpdateAsync(NameManufacturerDomain nameManufacturer);
    Task<bool> DeleteAsync(int id);
}