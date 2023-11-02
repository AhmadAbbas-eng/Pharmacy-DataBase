using AutoMapper;
using AutoMapper.Extensions.ExpressionMapping;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure;
using Infrastructure.Entities;
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
        
        //serviceCollection.AddScoped<ICustomerService, CustomerService>();

        //serviceCollection.AddAutoMapper(AppDomain.CurrentDomain.GetAssemblies());

        // TODO: manage the DbContext lifetime appropriately
        serviceCollection.AddDbContext<PharmacyDbContext>(options =>
            options.UseSqlServer("Server=localhost ,11433;Database=PharmacyEFMigrations;User Id=sa;Password=Itsmine123!;"));
        serviceCollection.AddScoped<IRepository<DomainWorkHours, int>, Repository<WorkHours, DomainWorkHours, int>>();
        serviceCollection.AddScoped<IRepository<SupplierDomain, int>, Repository<Supplier, SupplierDomain, int>>();
        serviceCollection.AddScoped<IRepository<SupplierPhone, int>, Repository<SupplierPhone, SupplierPhone, int>>();
        var config = new MapperConfiguration(cfg =>
        {
            cfg.AddExpressionMapping();
            
            cfg.CreateMap<Product, ProductDomian>();
            cfg.CreateMap<Supplier, SupplierDomain>();
            cfg.CreateMap<SupplierDomain, Supplier>();

            cfg.CreateMap<SupplierPhone, SupplierPhoneDomain>();
            cfg.CreateMap<SupplierPhoneDomain, SupplierPhone>();

        });

        IMapper mapper = config.CreateMapper();
        serviceCollection.AddSingleton(mapper);
        return serviceCollection.BuildServiceProvider();
    }
}
