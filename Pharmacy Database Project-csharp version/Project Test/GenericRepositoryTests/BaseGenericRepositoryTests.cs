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
    public async Task Add_AddsToDatabase()
    {
        var domainModel = CreateDomainModel();
        var expectedEntity = _mapper.Map<TDbModel>(domainModel);

        var id = _repository.AddAsync(domainModel).Result;
        
        var entityType = typeof(TDbModel);
        var idProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.Name.Contains("Id"));

        if (idProperty != null && typeof(TId).IsAssignableFrom(idProperty.PropertyType))
            idProperty.SetValue(expectedEntity, id);
        
        await _repository.SaveAsync();

        var entityDb = await _repository.GetByIdAsync(id);
        entityDb.Should().NotBeNull();

        entityDb.Should().BeEquivalentTo(expectedEntity, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public async Task  AddEntity_ReturnsCorrectId()
    {
        var domainModel = CreateDomainModel();

        var addedEntityId = await _repository.AddAsync(domainModel);
        await _repository.SaveAsync();
        addedEntityId.Should().NotBeNull();
    }

    [Fact]
    public async Task  AddEntity_ThrowsException_WhenNull()
    {
        await Assert.ThrowsAsync<ArgumentNullException>(() => _repository.AddAsync(null));
    }

    [Fact]
    public async Task GetEntityById_ReturnsNull_WhenInvalidId()
    {
        var model = await _repository.GetByIdAsync(default);
        model.Should().BeNull();
    }

    [Fact]
    public async Task GetAllEntities_ReturnsAllEntities()
    {
        var numberOfEntities = 5;
        var generatedEntities = new TModel[numberOfEntities];

        var entityType = typeof(TModel);

        for (var i = 0; i < numberOfEntities; i++)
        {
            generatedEntities[i] = CreateDomainModel();
            var idProperty = entityType.GetProperties()
                .FirstOrDefault(prop => prop.Name.Contains("Id"));

            var id = await _repository.AddAsync(generatedEntities[i]);

            if (idProperty != null && typeof(TId).IsAssignableFrom(idProperty.PropertyType))
                idProperty.SetValue(generatedEntities[i], id);
        }

        await _repository.SaveAsync();

        var entities = await _repository.GetAllAsync();
        entities.Should().NotBeNull();
        entities.Should().HaveCount(numberOfEntities);
        
        generatedEntities.Should().BeEquivalentTo(entities, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public async Task GetAllEntities_ReturnsEmpty_WhenNoEntities()
    {
        var entities = await _repository.GetAllAsync();
        entities.Should().BeEmpty();
    }

    [Fact]
    public async Task UpdateEntity_UpdatesExistingEntity()
    {
        var domainModel = CreateDomainModel();
        var addedProductId = await _repository.AddAsync(domainModel);
        await _repository.SaveAsync();

        var updatedProduct = await _repository.GetByIdAsync(addedProductId);
        
        var newDomainModel = CreateDomainModel();
        var entityType = typeof(TModel);
        var idProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.Name.Contains("Id"));
        
        foreach (var property in entityType.GetProperties())
            if (!property.Equals(idProperty))
                property.SetValue(updatedProduct, property.GetValue(newDomainModel));

        await _repository.UpdateAsync(updatedProduct);
        await _repository.SaveAsync();

        var productInDb = await _repository.GetByIdAsync(addedProductId);

        updatedProduct.Should().BeEquivalentTo(productInDb, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public async Task DeleteEntity_RemovesEntity()
    {
        var product = _fixture.Create<TModel>();
        var addedProduct = await _repository.AddAsync(product);
        await _repository.SaveAsync();

        await _repository.DeleteAsync(addedProduct);
        await _repository.SaveAsync();

        var foundProduct = await _repository.GetByIdAsync(addedProduct);
        foundProduct.Should().BeNull();
    }
}
