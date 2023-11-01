using Domain.Repositories.Interface;
using Infrastructure;
using Infrastructure.Repository;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;

namespace Console_Application;

public static class ServiceConfiguration
{
    public static ServiceProvider ConfigureServices()
    {
        var serviceCollection = new ServiceCollection();

        //serviceCollection.AddLogging(configure => configure.AddConsole());

        serviceCollection.AddScoped(typeof(IRepository<>), typeof(Repository<>));
        //serviceCollection.AddScoped<ICustomerService, CustomerService>();

        //serviceCollection.AddAutoMapper(AppDomain.CurrentDomain.GetAssemblies());

        // Database context configuration
        // Note: You will need to manage the DbContext lifetime appropriately
        serviceCollection.AddDbContext<PharmacyDbContext>(options =>
            options.UseSqlServer("Server=localhost;Trusted_Connection=True;"));

        return serviceCollection.BuildServiceProvider();
    }
}
