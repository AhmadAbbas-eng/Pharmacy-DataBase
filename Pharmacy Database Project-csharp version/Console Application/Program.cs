using AutoMapper;
using Console_Application;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure;
using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;

var serviceProvider = ServiceConfiguration.ConfigureServices();

var customerService = serviceProvider.GetService<IRepository<Customer>>();
customerService.Add(new Customer{CustomerNID = "12", Debt = 1.0, Name = "AhmadAbbas"});
serviceProvider.GetService<PharmacyDbContext>().SaveChanges();



var repository = serviceProvider.GetService<IRepository<Product>>();
repository.Add(new Product{Price = 1});
serviceProvider.GetService<PharmacyDbContext>().SaveChanges();

var repository1 = serviceProvider.GetService<IRepository<Batch>>();
repository1.Add(new Batch{ProductId = 1, ExpiryDate = DateTime.Now, ProductionDate = DateTime.Today});
serviceProvider.GetService<PharmacyDbContext>().SaveChanges();

var product = repository.GetById(1, p => p.Batches); // Assuming ProductId 1 exists

if (product != null)
{
    // Access the related batches for ProductId 1 through the navigation property
    var batchesForProduct1 = product.Batches;

    foreach (var batch in batchesForProduct1)
    {
        Console.WriteLine($"BatchId: {batch.BatchId}, ProductId: {batch.ProductId}, ExpiryDate: {batch.ExpiryDate}, ProductionDate: {batch.ProductionDate}");
    }
}

var config = new MapperConfiguration(cfg =>
{
    cfg.CreateMap<Product, ProductDomian>();
});

IMapper mapper = config.CreateMapper();

