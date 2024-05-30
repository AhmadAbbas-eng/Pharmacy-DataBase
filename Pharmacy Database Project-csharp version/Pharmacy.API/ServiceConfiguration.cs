using Domain.Services.Implementations;
using Domain.Services.Interfaces;
using Infrastructure;
using Microsoft.EntityFrameworkCore;
using Pharmacy.API.Configuration.Models;

namespace Pharmacy.API;

public static class ServiceConfiguration
{
    public static IServiceCollection AddConnectionStringConfiguration(this IServiceCollection services,
        IConfiguration configuration)
    {
        var config = new ApplicationConfiguration();
        configuration.GetSection("ApplicationConfiguration").Bind(config);

        if (config.DbConnection is null || string.IsNullOrEmpty(config.DbConnection.ConnectionString))
            throw new InvalidOperationException("Database configuration is not properly loaded.");

        services.AddSingleton<IPharmacyDbConnectionStringProvider>(provider =>
            new PharmacyDbConnectionStringProvider(config));
        return services;
    }

    public static IServiceCollection AddDatabaseConfiguration(this IServiceCollection services)
    {
        services.AddDbContext<PharmacyDbContext>((serviceProvider, options) =>
        {
            var connectionStringProvider = serviceProvider.GetRequiredService<IPharmacyDbConnectionStringProvider>();
            var connectionString = connectionStringProvider.GetPharmacyReadOnlyConnectionString();
            options.UseSqlServer(connectionString);
        }, ServiceLifetime.Transient);

        return services;
    }


    public static IServiceCollection AddLogging(IServiceCollection services)
    {
        services.AddLogging(configure => configure.AddConsole());
        return services;
    }

    public static IServiceCollection AddApplicationServices(this IServiceCollection serviceCollection)
    {
        serviceCollection.AddScoped<IWorkHoursService, WorkHoursService>();

        return serviceCollection;
    }
}