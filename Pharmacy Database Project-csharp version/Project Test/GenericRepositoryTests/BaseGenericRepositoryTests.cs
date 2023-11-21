using System;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using AutoFixture;
using AutoMapper;
using Domain.Repositories.Interface;
using FluentAssertions;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace Project_Test.GenericRepositoryUnitTests;

public abstract class BaseGenericRepositoryTests<TContext, TDbModel, TModel, TId>
    where TContext : DbContext, new()
    where TDbModel : class
    where TModel : class, new()
{
    private readonly Fixture _fixture;
    private readonly DbContextOptions<TContext> _options;
    protected TContext _context { get; }
    protected IRepository<TModel, TId> _repository { get; set; }
    protected IMapper _mapper { get; }
    
    public BaseGenericRepositoryTests()
    {
        _options = new DbContextOptionsBuilder<TContext>()
            .UseInMemoryDatabase(Guid.NewGuid().ToString())
            .LogTo(Console.WriteLine, LogLevel.Debug)
            .EnableSensitiveDataLogging()
            .Options;

        _context = (TContext)Activator.CreateInstance(typeof(TContext), _options);
        _context.Database.EnsureCreated();
        
        var mockMapperConfig = new MapperConfiguration(cfg =>
        {
            cfg.CreateMap<TDbModel, TModel>();
            cfg.CreateMap<TModel, TDbModel>();
        });
        
  
        _mapper = mockMapperConfig.CreateMapper();
        _fixture = new Fixture();
    }

    
    protected TModel CreateDomainModel()
    {
        var domain = _fixture.Build<TModel>()
            .Create();

        var dbModel = _mapper.Map<TDbModel>(domain);

        var entityType = typeof(TDbModel);

        var idProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.Name.Contains("Id"));

        if (idProperty != null && typeof(TId).IsAssignableFrom(idProperty.PropertyType))
            idProperty.SetValue(dbModel, null);
        else
            throw new InvalidOperationException(
                "The domain model does not have an 'Id' property of the expected type.");

        domain = _mapper.Map<TModel>(dbModel);
        return domain;
    }

    [Fact]
    public void Add_AddsToDatabase()
    {
        var domainModel = CreateDomainModel();
        var dbModel = _mapper.Map<TDbModel>(domainModel);

        var id = _repository.AddAsync(domainModel);
        _repository.SaveAsync();

        var entityDb = _context.Entry(dbModel);
        Assert.NotNull(entityDb);

        var expectedEntity = _mapper.Map<TDbModel>(domainModel);
        entityDb.Should().BeEquivalentTo(expectedEntity, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public void AddEntity_ReturnsCorrectId()
    {
        var domainModel = CreateDomainModel();

        var addedEntityId = _repository.AddAsync(domainModel);
        _repository.SaveAsync();

        Assert.NotEqual(default, addedEntityId);
    }

    [Fact]
    public void AddEntity_ThrowsException_WhenNull()
    {
        Assert.ThrowsAsync<ArgumentNullException>(() => _repository.AddAsync(null));
    }

    [Fact]
    public void GetEntityById_ReturnsNull_WhenInvalidId()
    {
        var model = _repository.GetByIdAsync(default).Result;
        Assert.Null(model);
    }

    [Fact]
    public void GetAllEntities_ReturnsAllEntities()
    {
        var numberOfEntities = 5;
        var generatedEntities = new TModel[numberOfEntities];

        var entityType = typeof(TModel);

        for (var i = 0; i < numberOfEntities; i++)
        {
            generatedEntities[i] = CreateDomainModel();
            var idProperty = entityType.GetProperties()
                .FirstOrDefault(prop => prop.Name.Contains("Id"));

            var id = _repository.AddAsync(generatedEntities[i]).Result;

            if (idProperty != null && typeof(TId).IsAssignableFrom(idProperty.PropertyType))
                idProperty.SetValue(generatedEntities[i], id);
        }

        _repository.SaveAsync();

        var entities = _repository.GetAllAsync().Result.ToList();

        Assert.NotEmpty(entities);
        Assert.Equal(numberOfEntities, entities.Count());
        generatedEntities.Should().BeEquivalentTo(entities, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public void GetAllEntities_ReturnsEmpty_WhenNoEntities()
    {
        var entities = _repository.GetAllAsync().Result;

        Assert.Empty(entities);
    }

    [Fact]
    public void UpdateEntity_UpdatesExistingEntity()
    {
        var domainModel = CreateDomainModel();
        var addedProductId = _repository.AddAsync(domainModel).Result;
        _repository.SaveAsync();

        var updatedProduct = _repository.GetByIdAsync(addedProductId).Result;
        var newDomainModel = CreateDomainModel();
        var entityType = typeof(TModel);
        var idProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.Name.Contains("Id"));
        
        foreach (var property in entityType.GetProperties())
            if (!property.Equals(idProperty))
                property.SetValue(updatedProduct, property.GetValue(newDomainModel));

        _repository.UpdateAsync(updatedProduct);
        _repository.SaveAsync();

        var productInDb = _repository.GetByIdAsync(addedProductId).Result;

        updatedProduct.Should().BeEquivalentTo(productInDb, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public void DeleteEntity_RemovesEntity()
    {
        var product = _fixture.Create<TModel>();
        var addedProduct = _repository.AddAsync(product).Result;
        _repository.SaveAsync();

        _repository.DeleteAsync(addedProduct);
        _repository.SaveAsync();

        var foundProduct = _repository.GetByIdAsync(addedProduct).Result;
        Assert.Null(foundProduct);
    }
}
