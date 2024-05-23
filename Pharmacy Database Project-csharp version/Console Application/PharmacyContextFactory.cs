using Infrastructure;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Design;
using Microsoft.Extensions.Configuration;
using Pharmacy.Configuration;

namespace Console_Application;

public class PharmacyContextFactory : IDesignTimeDbContextFactory<PharmacyDbContext>
{
    public PharmacyDbContext CreateDbContext(string[] args)
    {
        var configuration = new ConfigurationBuilder()
            .SetBasePath(Directory.GetCurrentDirectory())
            .AddJsonFile("appsettings.json", false, true)
            .Build();
        var config = new ApplicationConfiguration();

        configuration.GetSection("ApplicationConfiguration").Bind(config);

        var optionsBuilder = new DbContextOptionsBuilder<PharmacyDbContext>();
        optionsBuilder.UseSqlServer(config.DbConnection.ConnectionString);

        return new PharmacyDbContext(optionsBuilder.Options);
    }
}