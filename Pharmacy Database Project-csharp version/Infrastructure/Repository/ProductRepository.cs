using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class ProductRepository : IProductRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public ProductRepository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }


    public async Task<int> GetTotalAmountByProductIdAsync(int productId)
    {
        return await _context.Batches.Where(b => b.ProductId == productId)
            .SumAsync(b => b.Amount);
    }

    public async Task<List<ProductDomain>> GetOutOfStockProductsAsync()
    {
        var outOfStockDbProducts = await _context.Products
            .Where(p =>
                _context.Batches.Where(b => b.ProductId == p.ProductId).Sum(b => b.Amount) <= 0)
            .ToListAsync();

        var outOfStockProducts = _mapper.Map<List<ProductDomain>>(outOfStockDbProducts);
        return outOfStockProducts;
    }


    public async Task<double> CalculateAverageProductPriceAsync()
    {
        return await _context.Products.AverageAsync(p => p.Price);
    }
}