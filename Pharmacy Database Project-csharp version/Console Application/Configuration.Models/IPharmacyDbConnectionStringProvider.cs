namespace Pharmacy.Configuration;

public interface IPharmacyDbConnectionStringProvider
{
    string GetPharmacyReadOnlyConnectionString();
}