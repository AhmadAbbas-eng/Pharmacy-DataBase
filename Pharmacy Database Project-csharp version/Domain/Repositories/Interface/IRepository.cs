using System.Linq.Expressions;

namespace Domain.Repositories.Interface;

public interface IRepository<T>
{
    T GetById(int id, params Expression<Func<T, object>>[] includeProperties);
    IEnumerable<T> GetAll();
    void Add(T entity);
    void Update(T entity);
    void Delete(T entity);
    IEnumerable<T> Find(Expression<Func<T, bool>> predicate);
    void Save();
}
