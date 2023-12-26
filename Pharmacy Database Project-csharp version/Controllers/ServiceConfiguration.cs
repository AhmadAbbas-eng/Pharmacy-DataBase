using System.Reflection;
using System.Text;
using AutoMapper;
using AutoMapper.Extensions.ExpressionMapping;
using Controllers.Mappers;
using Controllers.Model.Dto;
using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Implementations;
using Domain.Services.Interfaces;
using Infrastructure;
using Infrastructure.Entities;
using Infrastructure.Repository;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using Pharmacy.Configuration;
using Serilog;

namespace Controllers;
public static class ServiceConfiguration
{

    public static IServiceCollection AddConnectionStringConfiguration(this IServiceCollection services,
        IConfiguration configuration)
    {
        var config = new ApplicationConfiguration();
        configuration.GetSection("ApplicationConfiguration").Bind(config);

        if (config.DbConnection is null || string.IsNullOrEmpty(config.DbConnection.ConnectionString))
        {
            throw new InvalidOperationException("Database configuration is not properly loaded.");
        }

        services.AddSingleton<IPharmacyDbConnectionStringProvider>(provider => new PharmacyDbConnectionStringProvider(config));
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

    
    
    public static IServiceCollection AddApplicationServices(this IServiceCollection serviceCollection)
    {
        serviceCollection.AddScoped<IRepository<SupplierDomain, int>, Repository<Supplier, SupplierDomain, int>>();
        serviceCollection.AddScoped<IRepository<EmployeeDomain, int>, Repository<Employee, EmployeeDomain, int>>();
        serviceCollection.AddScoped<IRepository<SupplierPhone, int>, Repository<SupplierPhone, SupplierPhone, int>>();
        serviceCollection.AddScoped<IRepository<TaxDomain, string>, Repository<Tax, TaxDomain, string>>();
        serviceCollection.AddScoped<IRepository<PaymentDomain, int>, Repository<Payment, PaymentDomain, int>>();
        serviceCollection.AddScoped<IRepository<ChequeDomain, int>, Repository<Cheque, ChequeDomain, int>>();
        serviceCollection.AddScoped<IRepository<WorkHoursDomain, int>, Repository<WorkHours, WorkHoursDomain, int>>();
        serviceCollection.AddScoped<IWorkHoursRepository, WorkHoursRepository>();
        serviceCollection.AddScoped<ICustomerRepository, CustomerRepository>();
        serviceCollection.AddScoped<ICustomerService, CustomerService>();
        
        serviceCollection.AddScoped<IEmployeeService, EmployeeService>();
        serviceCollection.AddScoped<IWorkHoursService, WorkHoursService>();

        return serviceCollection;
    }

    public static IServiceCollection AddAutoMapper(this IServiceCollection serviceCollection)
    {
        
        var config = new MapperConfiguration(cfg =>
        {
            cfg.AddExpressionMapping();

            var entityAssembly = Assembly.GetAssembly(typeof(BaseModel));
            var entityTypes = entityAssembly.GetTypes()
                .Where(t => t.Namespace == "Infrastructure.Entities");

            var modelAssembly = Assembly.GetAssembly(typeof(BaseModelDomain)); 
            var modelTypes = modelAssembly.GetTypes()
                .Where(t => t.Namespace == "Domain.Models");

            foreach (var entityType in entityTypes)
            {
                var modelName = entityType.Name + "Domain";
                var modelType = modelTypes.FirstOrDefault(t => t.Name == modelName);

                if (modelType != null)
                {
                    cfg.CreateMap(entityType, modelType);
                    cfg.CreateMap(modelType, entityType);
                }
            }
            
        });

        var mapper = config.CreateMapper();
        serviceCollection.AddSingleton(mapper);
        return serviceCollection;
    }

    public static IServiceCollection AddMapperly(this IServiceCollection serviceCollection)
    {
        serviceCollection.AddSingleton<EmployeeMapper>();
        serviceCollection.AddSingleton<CustomerMapper>();

        return serviceCollection;
    }
    
    public static IServiceCollection AddLoggerConfiguration(this IServiceCollection services)
    {
        Log.Logger = new LoggerConfiguration()
            .MinimumLevel.Debug()
            .WriteTo.Console()
            .WriteTo.File("logs/userinfo.txt", rollingInterval: RollingInterval.Day)
            .CreateLogger();

        services.AddLogging(loggingBuilder => 
            loggingBuilder.AddSerilog(dispose: true));

        return services;
    }

    public static IServiceCollection AddJwtAuthentication(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddAuthentication("Bearer")
            .AddJwtBearer(options =>
            {
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = true,
                    ValidateAudience = true,
                    ValidateIssuerSigningKey = true,
                    ValidIssuer = configuration["Authentication:Issuer"],
                    ValidAudience = configuration["Authentication:Audience"],
                    IssuerSigningKey = new SymmetricSecurityKey(
                        Encoding.ASCII.GetBytes(configuration["Authentication:SecretForKey"]))
                };
            });

        return services;
    }
    
    public static IServiceCollection AddSwaggerConfiguration(this IServiceCollection services)
    {
        services.AddSwaggerGen(options =>
        {
            options.SwaggerDoc("v1", new OpenApiInfo 
            { 
                Title = "Your API", 
                Version = "v1",
                Description = "Description for the API goes here."
            });

            var xmlFile = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
            var xmlPath = Path.Combine(AppContext.BaseDirectory, xmlFile);
            options.IncludeXmlComments(xmlPath);
        });

        return services;
    }
}
