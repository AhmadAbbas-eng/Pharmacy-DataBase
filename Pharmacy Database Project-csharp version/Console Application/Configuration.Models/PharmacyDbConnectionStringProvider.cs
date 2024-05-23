namespace Pharmacy.Configuration;

public class PharmacyDbConnectionStringProvider : IPharmacyDbConnectionStringProvider
{
    private readonly ApplicationConfiguration _appConfig;

    public PharmacyDbConnectionStringProvider(ApplicationConfiguration appConfig)
    {
        _appConfig = appConfig;
    }

    public string GetPharmacyReadOnlyConnectionString()
    {
        return _appConfig.DbConnection.ConnectionString;
    }
}