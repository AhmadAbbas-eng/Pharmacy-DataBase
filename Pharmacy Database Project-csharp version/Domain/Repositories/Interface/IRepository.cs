using System.Linq.Expressions;

namespace Domain.Repositories.Interface;

public interface IRepository<TModel, TId>
{
    Task<TModel> GetByIdAsync(TId id, params Expression<Func<TModel, object>>[] includeProperties);
    Task<IEnumerable<TModel>> GetAllAsync();
    Task<TId> AddAsync(TModel entity);
    Task AddAllAsync(IEnumerable<TModel> models);
    void Update(TModel entity);
    Task DeleteAsync(TId id);
    Task<IEnumerable<TModel>> FindAsync(params Expression<Func<TModel, bool>>[] predicate);
    Task SaveAsync();
}