using AutoMapper;
using Console_Application;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure;
using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;

var serviceProvider = ServiceConfiguration.ConfigureServices();

var a= serviceProvider.GetService<PharmacyDbContext>();

var repository1 = serviceProvider.GetService<IRepository<SupplierDomain, int>>();
var repository2 = serviceProvider.GetService<IRepository<SupplierPhone, int>>();

// repository1.Add(new SupplierDomain{Name = "Ibrahim", Address = "R", Email = "a"});
// repository2.Add(new SupplierPhone{SupplierId = 2, Phone = "02999"});
// serviceProvider.GetService<PharmacyDbContext>().SaveChanges();

var supplier = repository1.GetById(2, p => p.SupplierPhones);

if (supplier != null)
{
    var batchesForProduct1 = supplier.SupplierPhones;

    Console.WriteLine(supplier.Name);
    foreach (var batch in batchesForProduct1)
    {
        Console.WriteLine($"BatchId: {batch.SupplierId}, ProductId: {batch.Phone}");
    }
}

var config = new MapperConfiguration(cfg =>
{
    cfg.CreateMap<Product, ProductDomian>();
});

IMapper mapper = config.CreateMapper();

