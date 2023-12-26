using AutoMapper;

using Console_Application;
using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Implementations;
using Domain.Services.Interfaces;
using Infrastructure;
using Infrastructure.Entities;
using Infrastructure.Repository;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;
using Pharmacy.Configuration;

var services = new ServiceCollection();


var configuration = new ConfigurationBuilder()
    .SetBasePath(Directory.GetCurrentDirectory())
    .AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
    .Build();

services.AddConnectionStringConfiguration(configuration)
    .AddDatabaseConfiguration()
    .AddLogging()
    .AddApplicationServices()
    .AddAutoMapper();
    
var serviceProvider = services.BuildServiceProvider();

serviceProvider.GetService<PharmacyDbContext>().Database.EnsureCreated();
serviceProvider.GetService<IWorkHoursService>().CalculateMonthlyWagesAsync(1, 6, 2001);