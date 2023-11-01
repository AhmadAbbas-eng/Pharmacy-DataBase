namespace Domain.Repositories.Interface;

public interface INameMenuRepository
{
    IEnumerable<string> GetAllDistinctManufacturers();
    Task UpdateManufacturerNameAsync(string oldName, string newName);
}