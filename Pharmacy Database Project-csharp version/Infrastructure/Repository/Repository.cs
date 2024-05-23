using System.ComponentModel.DataAnnotations;
using System.Linq.Expressions;
using System.Reflection;
using AutoMapper;
using Domain.Repositories.Interface;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class Repository<TDbModel, TModel, TId> : IRepository<TModel, TId>
    where TModel : class
    where TDbModel : class
{
    private readonly PharmacyDbContext _context;
    private readonly DbSet<TDbModel> _dbSet;
    private readonly IMapper _mapper;

    public Repository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _dbSet = context.Set<TDbModel>();
        _mapper = mapper;
    }

    public async Task<TModel> GetByIdAsync(TId id, params Expression<Func<TModel, object>>[] includeProperties)
    {
        var query = includeProperties
            .Select(includeProperty => MapperExtensions.Convert<TModel, TDbModel>(includeProperty))
            .Aggregate<Expression<Func<TDbModel, object>>?, IQueryable<TDbModel>>(_dbSet,
                (current, includeExpression) => current.Include(includeExpression));

        var entityType = typeof(TDbModel);

        var primaryKeyProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.CustomAttributes.Any(attr => attr.AttributeType == typeof(KeyAttribute)));

        if (primaryKeyProperty is null)
            throw new InvalidOperationException($"No primary key attribute found on the entity {entityType}.");

        var parameter = Expression.Parameter(entityType, "x");
        Expression propertyAccess = Expression.Property(parameter, primaryKeyProperty);
        Expression idMatch = Expression.Equal(propertyAccess, Expression.Constant(id));
        var lambda = Expression.Lambda<Func<TDbModel, bool>>(idMatch, parameter);

        var result = await query.FirstOrDefaultAsync(lambda);
        return _mapper.Map<TModel>(result);
    }

    public async Task<IEnumerable<TModel>> GetAllAsync()
    {
        var result = await _dbSet.ToListAsync();
        return _mapper.Map<List<TModel>>(result);
    }

    public async Task<TId> AddAsync(TModel entity)
    {
        if (entity is null) throw new ArgumentNullException("entity", "Entity Must Not be Null");

        var dbModel = _mapper.Map<TDbModel>(entity);
        await _dbSet.AddAsync(dbModel);
        await _context.SaveChangesAsync();

        var primaryKey = _context.Entry(dbModel).Metadata.FindPrimaryKey();
        var primaryKeyProperty = primaryKey.Properties[0];
        var primaryKeyValue = _context.Entry(dbModel).Property(primaryKeyProperty.Name).CurrentValue;

        return (TId)primaryKeyValue;
    }

    public async Task AddAllAsync(IEnumerable<TModel> models)
    {
        var entities = models.Select(model => _mapper.Map<TDbModel>(model));
        await _dbSet.AddRangeAsync(entities);
        await _context.SaveChangesAsync();
    }

    public void Update(TModel entity)
    {
        var predicates = BuildPredicatesForNonNullProperties(entity);

        IQueryable<TDbModel> query = _dbSet;

        foreach (var predicate in predicates)
        {
            var predicateExpression = _mapper.ConvertPredicate<TModel, TDbModel>(predicate);
            query.Where(predicateExpression);
        }

        var dbEntity = query.ToList().FirstOrDefault();

        if (dbEntity != null)
        {
            _context.Entry(dbEntity).State = EntityState.Detached;
            _context.Entry(_mapper.Map<TDbModel>(entity)).State = EntityState.Modified;
        }
        else
        {
            throw new ArgumentException("Entity was not Found", "entity");
        }
    }

    public async Task DeleteAsync(TId id)
    {
        var dbModel = await _dbSet.FindAsync(id);
        _dbSet.Remove(dbModel);
    }

    public async Task<IEnumerable<TModel>> FindAsync(params Expression<Func<TModel, bool>>[] predicates)
    {
        IQueryable<TDbModel> query = _dbSet;

        foreach (var predicate in predicates)
        {
            var predicateExpression = _mapper.ConvertPredicate<TModel, TDbModel>(predicate);
            query = query.Where(predicateExpression);
        }

        var result = await query.ToListAsync();
        return _mapper.Map<List<TModel>>(result);
    }

    public async Task SaveAsync()
    {
        await _context.SaveChangesAsync();
    }

    public static Expression<Func<TModel, bool>>[] BuildPredicatesForNonNullProperties<TModel>(TModel entity)
        where TModel : class
    {
        var predicates = new List<Expression<Func<TModel, bool>>>();
        var entityType = typeof(TModel);

        var properties = entityType.GetProperties(BindingFlags.Public | BindingFlags.Instance);

        foreach (var property in properties)
        {
            var propertyValue = property.GetValue(entity);
            if (propertyValue != null)
            {
                var parameterExp = Expression.Parameter(entityType, "type");
                var propertyExp = Expression.Property(parameterExp, property);
                var valueExp = Expression.Constant(propertyValue);

                if (property.PropertyType.IsValueType || property.PropertyType == typeof(string))
                {
                    var equalsExp = Expression.Equal(propertyExp, valueExp);
                    var lambda = Expression.Lambda<Func<TModel, bool>>(equalsExp, parameterExp);
                    predicates.Add(lambda);
                }
            }
        }

        return predicates.ToArray();
    }
}