using Domain.Models;

namespace Domain.Services.Interfaces;

public interface INameManufacturerService
{
    Task<IEnumerable<string>> GetDistinctNamesAsync();
    Task UpdateNameAsync(string oldName, string newName);
    Task<IEnumerable<NameManufacturerDomain>> GetAllNameManufacturersAsync();
    Task<NameManufacturerDomain?> GetNameManufacturerByIdAsync(int id);
    Task<NameManufacturerDomain> AddNameManufacturerAsync(NameManufacturerDomain nameManufacturer);
    Task<NameManufacturerDomain?> UpdateNameManufacturerAsync(NameManufacturerDomain nameManufacturer);
    Task<bool> DeleteNameManufacturerAsync(int id);
}