using System.Linq.Expressions;

namespace Domain.Repositories.Interface;

public interface IRepository<TModel, TId> 
{
    TModel GetById(TId id, params Expression<Func<TModel, object>>[] includeProperties);
    Task<TModel> GetByIdAsync(TId id, params Expression<Func<TModel, object>>[] includeProperties);
    IEnumerable<TModel> GetAll();
    Task<IEnumerable<TModel>> GetAllAsync();
    TId Add(TModel entity);
    Task<TId> AddAsync(TModel entity);
    void Update(TModel entity);
    void Delete(TModel entity);
    IEnumerable<TModel> Find(params Expression<Func<TModel, bool>>[] predicate);
    Task<IEnumerable<TModel>> FindAsync(params Expression<Func<TModel, bool>>[] predicate);
    void Save();
    Task SaveAsync();
}
