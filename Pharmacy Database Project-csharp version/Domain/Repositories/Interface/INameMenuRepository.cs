namespace Domain.Repositories.Interface;

public interface INameMenuRepository
{
    Task<IEnumerable<string>> GetAllDistinctManufacturersAsync();
    Task UpdateManufacturerNameAsync(string oldName, string newName);
}