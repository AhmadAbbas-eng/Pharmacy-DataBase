using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class ProductRepository : Repository<Product, ProductDomain, int>, IProductRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public ProductRepository(PharmacyDbContext context, IMapper mapper) : base(context, mapper)
    {
        _context = context;
        _mapper = mapper;
    }


    public async Task<int> GetTotalAmountByIdAsync(int productId)
    {
        return await _context.Batches.Where(b => b.ProductId == productId)
            .SumAsync(b => b.Amount);
    }

    public async Task<List<ProductDomain>> GetOutOfStockAsync()
    {
        var outOfStockDbProducts = await _context.Products
            .Where(p =>
                _context.Batches.Where(b => b.ProductId == p.ProductId).Sum(b => b.Amount) <= 0)
            .ToListAsync();

        var outOfStockProducts = _mapper.Map<List<ProductDomain>>(outOfStockDbProducts);
        return outOfStockProducts;
    }


    public async Task<double> CalculateAveragePriceAsync()
    {
        return await _context.Products.AverageAsync(p => p.Price);
    }
}