using Console_Application;
using Domain.Services.Interfaces;
using Infrastructure;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

var services = new ServiceCollection();


var configuration = new ConfigurationBuilder()
    .SetBasePath(Directory.GetCurrentDirectory())
    .AddJsonFile("appsettings.json", false, true)
    .Build();

services.AddConnectionStringConfiguration(configuration)
    .AddDatabaseConfiguration()
    .AddLogging()
    .AddApplicationServices()
    .AddAutoMapper();

var serviceProvider = services.BuildServiceProvider();

serviceProvider.GetService<PharmacyDbContext>().Database.EnsureCreated();
serviceProvider.GetService<IWorkHoursService>().CalculateMonthlyWagesAsync(1, 6, 2001);