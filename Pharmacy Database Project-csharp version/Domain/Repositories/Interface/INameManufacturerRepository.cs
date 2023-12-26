using Domain.Models;

namespace Domain.Repositories.Interface;

public interface INameManufacturerRepository : IRepository<NameManufacturerDomain, int>
{
    Task<IEnumerable<string>> GetDistinctAsync();
    Task UpdateAsync(string oldName, string newName);
}