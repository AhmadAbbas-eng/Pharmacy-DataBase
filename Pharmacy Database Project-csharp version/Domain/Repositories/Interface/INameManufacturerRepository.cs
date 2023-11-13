using Domain.Models;

namespace Domain.Repositories.Interface;

public interface INameManufacturerRepository : IRepository<NameManufacturerDomain, int>
{
    Task<IEnumerable<string>> GetAllDistinctManufacturersAsync();
    Task UpdateManufacturerNameAsync(string oldName, string newName);
}