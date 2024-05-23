using System.ComponentModel.DataAnnotations;
using AutoFixture;
using AutoMapper;
using Domain.Repositories.Interface;
using FluentAssertions;
using Microsoft.EntityFrameworkCore;

namespace Project_Test.GenericRepositoryUnitTests;

public abstract class BaseGenericRepositoryTests<TContext, TDbModel, TModel, TId>
    where TContext : DbContext, new()
    where TDbModel : class
    where TModel : class, new()
{
    private readonly Fixture _fixture;
    private readonly DbContextOptions<TContext> _options;

    public BaseGenericRepositoryTests()
    {
        _options = new DbContextOptionsBuilder<TContext>()
            .UseInMemoryDatabase(Guid.NewGuid().ToString())
            .LogTo(Console.WriteLine)
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

    protected TContext _context { get; }
    protected IRepository<TModel, TId> _repository { get; set; }
    protected IMapper _mapper { get; }


    protected TModel CreateDomainModel()
    {
        var domain = _fixture.Build<TModel>()
            .Create();

        var dbModel = _mapper.Map<TDbModel>(domain);

        var entityType = typeof(TDbModel);

        var idProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.CustomAttributes.Any(attr => attr.AttributeType == typeof(KeyAttribute)));

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
        var dbModel = _mapper.Map<TDbModel>(domainModel);

        var id = await _repository.AddAsync(domainModel);
        await _repository.SaveAsync();

        var entityDb = _context.Entry(dbModel);
        Assert.NotNull(entityDb);

        var expectedEntity = _mapper.Map<TDbModel>(domainModel);
        entityDb.Should().BeEquivalentTo(expectedEntity, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public async Task AddEntity_ReturnsCorrectId()
    {
        var domainModel = CreateDomainModel();

        var addedEntityId = await _repository.AddAsync(domainModel);
        await _repository.SaveAsync();

        Assert.NotEqual(default, addedEntityId);
    }

    [Fact]
    public async Task AddEntity_ThrowsException_WhenNull()
    {
        await Assert.ThrowsAsync<ArgumentNullException>(() => _repository.AddAsync(null));
    }

    [Fact]
    public async Task GetEntityById_ReturnsNull_WhenInvalidId()
    {
        var model = await _repository.GetByIdAsync(default);
        Assert.Null(model);
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
                .FirstOrDefault(prop => prop.CustomAttributes.Any(attr => attr.AttributeType == typeof(KeyAttribute)));

            var id = await _repository.AddAsync(generatedEntities[i]);

            if (idProperty != null && typeof(TId).IsAssignableFrom(idProperty.PropertyType))
                idProperty.SetValue(generatedEntities[i], id);
        }

        await _repository.SaveAsync();

        var entities = await _repository.GetAllAsync();

        Assert.NotEmpty(entities);
        Assert.Equal(numberOfEntities, entities.Count());
        generatedEntities.Should().BeEquivalentTo(entities, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public async Task GetAllEntities_ReturnsEmpty_WhenNoEntities()
    {
        var entities = await _repository.GetAllAsync();

        Assert.Empty(entities);
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
            .FirstOrDefault(prop => prop.CustomAttributes.Any(attr => attr.AttributeType == typeof(KeyAttribute)));

        foreach (var property in entityType.GetProperties().Where(property => !property.Equals(idProperty)))
            property.SetValue(updatedProduct, property.GetValue(newDomainModel));

        _repository.Update(updatedProduct);
        await _repository.SaveAsync();

        var productInDb = await _repository.GetByIdAsync(addedProductId);

        updatedProduct.Should().BeEquivalentTo(productInDb, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public async Task UpdateEntity_UpdatesShouldThroughException()
    {
        await Assert.ThrowsAsync<ArgumentException>(() =>
        {
            var domainModel = CreateDomainModel();
            _repository.Update(domainModel);
            return _repository.SaveAsync();
        });
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
        Assert.Null(foundProduct);
    }

    [Fact]
    public async Task DeleteEntity_ThrowsException_WhenEntityDoesNotExist()
    {
        var id = _fixture.Create<TId>();

        await Assert.ThrowsAsync<ArgumentNullException>(() => _repository.DeleteAsync(id));
    }
}