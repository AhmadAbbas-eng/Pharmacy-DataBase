namespace Domain.Repositories.Interface;

public interface INameManufacturerRepository
{
    Task<IEnumerable<string>> GetDistinctAsync();
    Task UpdateAsync(string oldName, string newName);
}