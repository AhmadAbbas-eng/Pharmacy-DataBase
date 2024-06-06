using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class ProductRepository : IProductRepository
{
    private readonly PharmacyDbContext _context;
    private readonly ProductMapper _mapper;

    public ProductRepository(PharmacyDbContext context, ProductMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public Task<int> GetTotalAmountByIdAsync(int productId)
    {
        return _context.Batches.Where(b => b.ProductId == productId)
            .SumAsync(b => b.Amount);
    }

    public async Task<List<ProductDomain>> GetOutOfStockAsync()
    {
        var outOfStockDbProducts = await _context.Products
            .Where(p =>
                _context.Batches.Where(b => b.ProductId == p.ProductId).Sum(b => b.Amount) <= 0)
            .ToListAsync();

        return outOfStockDbProducts.Select(p => _mapper.MapToDomain(p)).ToList();
    }

    public Task<double> CalculateAveragePriceAsync()
    {
        return _context.Products.AverageAsync(p => p.Price);
    }

    public async Task<IEnumerable<ProductDomain>> GetAllAsync()
    {
        var products = await _context.Products.ToListAsync();
        return products.Select(p => _mapper.MapToDomain(p));
    }

    public async Task<ProductDomain?> GetByIdAsync(int id)
    {
        var product = await _context.Products.FindAsync(id);
        return product == null ? null : _mapper.MapToDomain(product);
    }

    public async Task<ProductDomain> AddAsync(ProductDomain product)
    {
        var entity = _mapper.MapToEntity(product);
        _context.Products.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<ProductDomain?> UpdateAsync(ProductDomain product)
    {
        var entity = await _context.Products.FindAsync(product.Id);
        if (entity == null) return null;

        _mapper.MapToEntity(product, entity);
        _context.Products.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.Products.FindAsync(id);
        if (entity == null) return false;

        _context.Products.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}