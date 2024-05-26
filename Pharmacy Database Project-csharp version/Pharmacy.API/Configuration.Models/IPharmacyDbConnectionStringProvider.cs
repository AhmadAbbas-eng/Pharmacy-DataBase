namespace Pharmacy.API.Configuration.Models;

public interface IPharmacyDbConnectionStringProvider
{
    string GetPharmacyReadOnlyConnectionString();
}