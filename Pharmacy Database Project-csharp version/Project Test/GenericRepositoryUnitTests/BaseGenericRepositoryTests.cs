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
    public void Add_AddsToDatabase()
    {
        var domainModel = CreateDomainModel();
        var dbModel = _mapper.Map<TDbModel>(domainModel);

        var id = _repository.Add(domainModel);
        _repository.Save();

        var entityDb = _context.Entry(dbModel);
        Assert.NotNull(entityDb);

        var expectedEntity = _mapper.Map<TDbModel>(domainModel);
        entityDb.Should().BeEquivalentTo(expectedEntity, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public void AddEntity_ReturnsCorrectId()
    {
        var domainModel = CreateDomainModel();

        var addedEntityId = _repository.Add(domainModel);
        _repository.Save();

        Assert.NotEqual(default, addedEntityId);
    }

    [Fact]
    public void AddEntity_ThrowsException_WhenNull()
    {
        Assert.Throws<ArgumentNullException>(() => _repository.Add(null));
    }

    [Fact]
    public void GetEntityById_ReturnsNull_WhenInvalidId()
    {
        var model = _repository.GetById(default);
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
                .FirstOrDefault(prop => prop.CustomAttributes.Any(attr => attr.AttributeType == typeof(KeyAttribute)));

            var id = _repository.Add(generatedEntities[i]);

            if (idProperty != null && typeof(TId).IsAssignableFrom(idProperty.PropertyType))
                idProperty.SetValue(generatedEntities[i], id);
        }

        _repository.Save();

        var entities = _repository.GetAll();

        Assert.NotEmpty(entities);
        Assert.Equal(numberOfEntities, entities.Count());
        generatedEntities.Should().BeEquivalentTo(entities, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public void GetAllEntities_ReturnsEmpty_WhenNoEntities()
    {
        var entities = _repository.GetAll();

        Assert.Empty(entities);
    }

    [Fact]
    public void UpdateEntity_UpdatesExistingEntity()
    {
        var domainModel = CreateDomainModel();
        var addedProductId = _repository.Add(domainModel);
        _repository.Save();

        var updatedProduct = _repository.GetById(addedProductId);
        var newDomainModel = CreateDomainModel();
        var entityType = typeof(TModel);
        var idProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.CustomAttributes.Any(attr => attr.AttributeType == typeof(KeyAttribute)));

        foreach (var property in entityType.GetProperties())
            if (!property.Equals(idProperty))
                property.SetValue(updatedProduct, property.GetValue(newDomainModel));

        _repository.Update(updatedProduct);
        _repository.Save();

        var productInDb = _repository.GetById(addedProductId);

        updatedProduct.Should().BeEquivalentTo(productInDb, options => options.ExcludingMissingMembers());
    }

    [Fact]
    public void UpdateEntity_UpdatesShouldThroughException()
    {
        Assert.Throws<ArgumentException>(() =>
        {
            var domainModel = CreateDomainModel();
            _repository.Update(domainModel);
            _repository.Save();
        });
    }

    [Fact]
    public void DeleteEntity_RemovesEntity()
    {
        var product = _fixture.Create<TModel>();
        var addedProduct = _repository.Add(product);
        _repository.Save();

        _repository.Delete(product);
        _repository.Save();

        var foundProduct = _repository.GetById(addedProduct);
        Assert.Null(foundProduct);
    }

    [Fact]
    public void DeleteEntity_ThrowsException_WhenEntityDoesNotExist()
    {
        var product = CreateDomainModel();

        Assert.Throws<ArgumentException>(() => _repository.Delete(product));
    }
}