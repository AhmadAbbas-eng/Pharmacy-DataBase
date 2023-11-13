﻿using AutoMapper;
using AutoMapper.Extensions.ExpressionMapping;
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

namespace Controllers;

public static class ServiceConfiguration
{
    public static ServiceProvider ConfigureServices(IServiceCollection serviceCollection)
    {
        

        //serviceCollection.AddLogging(configure => configure.AddConsole());

        //serviceCollection.AddScoped<ICustomerService, CustomerService>();

        //serviceCollection.AddAutoMapper(AppDomain.CurrentDomain.GetAssemblies());

        // TODO: manage the DbContext lifetime appropriately
        serviceCollection.AddDbContext<PharmacyDbContext>(options =>
            options.UseSqlServer("Server=localhost,11433;Database=Pharmacy;User Id=sa;Password=Itsmine123!;"));
        serviceCollection.AddScoped<IRepository<WorkHoursDomain, int>, Repository<WorkHours, WorkHoursDomain, int>>();
        serviceCollection.AddScoped<IRepository<SupplierDomain, int>, Repository<Supplier, SupplierDomain, int>>();
        serviceCollection.AddScoped<IRepository<EmployeeDomain, int>, Repository<Employee, EmployeeDomain, int>>();
        serviceCollection.AddScoped<IRepository<SupplierPhone, int>, Repository<SupplierPhone, SupplierPhone, int>>();
        serviceCollection.AddScoped<IRepository<TaxDomain, string>, Repository<Tax, TaxDomain, string>>();
        serviceCollection.AddScoped<IRepository<PaymentDomain, int>, Repository<Payment, PaymentDomain, int>>();
        serviceCollection.AddScoped<IRepository<ChequeDomain, int>, Repository<Cheque, ChequeDomain, int>>();
        serviceCollection.AddScoped<IWorkHoursService, WorkHoursService>();
        serviceCollection.AddScoped<IEmployeeService, EmployeeService>();
        
        var config = new MapperConfiguration(cfg =>
        {
            cfg.AddExpressionMapping();

            cfg.CreateMap<Product, ProductDomain>();
            cfg.CreateMap<Supplier, SupplierDomain>();
            cfg.CreateMap<SupplierDomain, Supplier>();

            cfg.CreateMap<EmployeeDomain, Employee>();
            cfg.CreateMap<Employee, EmployeeDomain>();

            cfg.CreateMap<SupplierPhone, SupplierPhoneDomain>();
            cfg.CreateMap<SupplierPhoneDomain, SupplierPhone>();

            cfg.CreateMap<WorkHours, WorkHoursDomain>();
            cfg.CreateMap<WorkHoursDomain, WorkHours>();

            cfg.CreateMap<TaxDomain, Tax>();
            cfg.CreateMap<Tax, TaxDomain>();

            cfg.CreateMap<ChequeDomain, Cheque>();
            cfg.CreateMap<Cheque, ChequeDomain>();

            cfg.CreateMap<PaymentDomain, Payment>();
            cfg.CreateMap<Payment, PaymentDomain>();

            cfg.CreateMap<EmployeeDomain, EmployeeDto>();
            cfg.CreateMap<EmployeeDto, EmployeeDomain>();

        });

        var mapper = config.CreateMapper();
        serviceCollection.AddSingleton(mapper);
        return serviceCollection.BuildServiceProvider();
    }
}