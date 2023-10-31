// See https://aka.ms/new-console-template for more information

using Domain_Layer;
using Microsoft.EntityFrameworkCore;

var contextOptions = new DbContextOptionsBuilder<PharmacyDbContext>()
    .UseSqlServer(@"Server=localhost, 11433;Database=PharmacyEFMigrations;User Id=sa;Password=Itsmine123!;")
    .Options;
using var context = new PharmacyDbContext(contextOptions);    
Console.WriteLine(context.Database.EnsureCreated());