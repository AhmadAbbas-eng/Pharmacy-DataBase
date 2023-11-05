using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;

namespace Project_Test;

public class BaseTest<TContext> : IDisposable where TContext : DbContext, new()
{
    private readonly DbContextOptions<TContext> _options;

    public BaseTest()
    {
        _options = new DbContextOptionsBuilder<TContext>()
            .UseInMemoryDatabase(Guid.NewGuid().ToString())
            .Options;

        Context = (TContext)Activator.CreateInstance(typeof(TContext), _options);
        Context.Set<Product>();
        Context.Database.EnsureCreated();
    }

    protected TContext Context { get; }

    public void Dispose()
    {
        Context.Database.EnsureDeleted();
        Context.Dispose();
    }
}