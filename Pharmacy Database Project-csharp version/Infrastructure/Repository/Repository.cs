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
    private readonly IMapper _mapper;
    private readonly DbSet<TDbModel> _dbSet;

    public Repository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _dbSet = context.Set<TDbModel>();
        _mapper = mapper;
    }


    
    private Expression<Func<TDbModel, object>> Convert(Expression<Func<TModel, object>> sourceMember)
    {
        var memberName = ((MemberExpression)sourceMember.Body).Member.Name;
        var parameter = Expression.Parameter(typeof(TDbModel), "src");
        var property = Expression.Property(parameter, memberName);
        return Expression.Lambda<Func<TDbModel, object>>(property, parameter);
    }
    
    public TModel GetById(TId id, params Expression<Func<TModel, object>>[] includeProperties)
    {
        IQueryable<TDbModel> query = _dbSet;

        foreach (var includeProperty in includeProperties)
        {
            var includeExpression = Convert(includeProperty);
            query = query.Include(includeExpression);
        }

        Type entityType = typeof(TDbModel);

        PropertyInfo primaryKeyProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.CustomAttributes.Any(attr => attr.AttributeType == typeof(KeyAttribute)));

        if (primaryKeyProperty == null)
        {
            throw new InvalidOperationException($"No primary key attribute found on the entity {entityType}.");
        }

        ParameterExpression parameter = Expression.Parameter(entityType, "x");
        Expression propertyAccess = Expression.Property(parameter, primaryKeyProperty);
        Expression idMatch = Expression.Equal(propertyAccess, Expression.Constant(id));
        var lambda = Expression.Lambda<Func<TDbModel, bool>>(idMatch, parameter);

        var result = query.FirstOrDefault(lambda);
        return _mapper.Map<TModel>(result);
    }

    public async Task<TModel> GetByIdAsync(TId id, params Expression<Func<TModel, object>>[] includeProperties)
    {
        IQueryable<TDbModel> query = _dbSet;

        foreach (var includeProperty in includeProperties)
        {
            var includeExpression = Convert(includeProperty);
            query = query.Include(includeExpression);
        }

        Type entityType = typeof(TDbModel);

        PropertyInfo primaryKeyProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.CustomAttributes.Any(attr => attr.AttributeType == typeof(KeyAttribute)));

        if (primaryKeyProperty == null)
        {
            throw new InvalidOperationException($"No primary key attribute found on the entity {entityType}.");
        }

        ParameterExpression parameter = Expression.Parameter(entityType, "x");
        Expression propertyAccess = Expression.Property(parameter, primaryKeyProperty);
        Expression idMatch = Expression.Equal(propertyAccess, Expression.Constant(id));
        var lambda = Expression.Lambda<Func<TDbModel, bool>>(idMatch, parameter);

        var result = await query.FirstOrDefaultAsync(lambda);
        return _mapper.Map<TModel>(result);
    }
    
    public IEnumerable<TModel> GetAll()
    {
        var list = _dbSet.ToList();
        return _mapper.Map<List<TModel>>(list);
    }
    
    public async Task<IEnumerable<TModel>> GetAllAsync()
    {
        var result =  await _dbSet.ToListAsync();
        return _mapper.Map<List<TModel>>(result);
    }
    

    public void Add(TModel entity)
    {
        _dbSet.Add(_mapper.Map<TDbModel>(entity));
    }
    
    public async Task AddAsync(TModel entity)
    {
        await _dbSet.AddAsync(_mapper.Map<TDbModel>(entity));
    }
    
    public void Update(TModel entity)
    {
        var dbEntity = _mapper.Map<TDbModel>(entity);
        _dbSet.Attach(dbEntity);
        _context.Entry(dbEntity).State = EntityState.Modified;
    }

    public void Delete(TModel entity)
    {
        var dbEntity = _mapper.Map<TDbModel>(entity);

        if (_context.Entry(dbEntity).State == EntityState.Detached)
        {
            _dbSet.Attach(dbEntity);
        }
        _dbSet.Remove(dbEntity);
    }
    
    public IEnumerable<TModel> Find(Expression<Func<TModel, bool>> predicate)
    {
        var result = _dbSet.Where(_mapper.Map<Expression<Func<TDbModel, bool>>>(predicate)).ToList();
        return _mapper.Map<List<TModel>>(result);
    }
        
    public async Task<IEnumerable<TModel>> FindAsync(Expression<Func<TModel, bool>> predicate)
    {
        var result =  await _dbSet.Where(_mapper.Map<Expression<Func<TDbModel, bool>>>(predicate)).ToListAsync();
        return _mapper.Map<List<TModel>>(result);

    }
    public void Save()
    {
        _context.SaveChanges();
    }
    
    public async Task SaveAsync()
    {
        await _context.SaveChangesAsync();
    }
}
