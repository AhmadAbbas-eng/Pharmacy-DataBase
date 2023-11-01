using System.ComponentModel.DataAnnotations;
using System.Linq.Expressions;
using System.Reflection;
using Domain.Repositories.Interface;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;


public class Repository<T> : IRepository<T> where T : class
{
    private readonly PharmacyDbContext _context;
    private readonly DbSet<T> _dbSet;

    public Repository(PharmacyDbContext context)
    {
        _context = context;
        _dbSet = context.Set<T>();
    }


    public T GetById(int id, params Expression<Func<T, object>>[] includeProperties)
    {
        IQueryable<T> query = _dbSet;

        foreach (var includeProperty in includeProperties)
        {
            query = query.Include(includeProperty);
        }

        Type entityType = typeof(T);

        PropertyInfo primaryKeyProperty = entityType.GetProperties()
            .FirstOrDefault(prop => prop.CustomAttributes.Any(attr => attr.AttributeType == typeof(KeyAttribute)));

        if (primaryKeyProperty == null)
        {
            throw new InvalidOperationException($"No primary key attribute found on the entity {entityType}.");
        }

        ParameterExpression parameter = Expression.Parameter(entityType, "x");
        Expression propertyAccess = Expression.Property(parameter, primaryKeyProperty);
        Expression idMatch = Expression.Equal(propertyAccess, Expression.Constant(id));
        var lambda = Expression.Lambda<Func<T, bool>>(idMatch, parameter);

        return query.FirstOrDefault(lambda);
    }

    public IEnumerable<T> GetAll()
    {
        return _dbSet.ToList();
    }

    public void Add(T entity)
    {
        _dbSet.Add(entity);
    }

    public void Update(T entity)
    {
        _dbSet.Attach(entity);
        _context.Entry(entity).State = EntityState.Modified;
    }

    public void Delete(T entity)
    {
        if (_context.Entry(entity).State == EntityState.Detached)
        {
            _dbSet.Attach(entity);
        }
        _dbSet.Remove(entity);
    }
    
    public IEnumerable<T> Find(Expression<Func<T, bool>> predicate)
    {
        return _dbSet.Where(predicate).ToList();
    }
    
    public void Save()
    {
        _context.SaveChanges();
    }
}
