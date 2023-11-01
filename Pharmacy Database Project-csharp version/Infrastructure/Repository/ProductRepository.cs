using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;
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

    
    public int GetTotalAmountByProductId(int productId)
    {
        return _context.Batches.Where(b => b.ProductId == productId)
            .Sum(b => b.Amount);
    }

    public List<ProductDomian> GetOutOfStockProducts()
    {

        var outOfStockDbProducts = _context.Products
            .Where(p => 
                !(_context.Batches.Where(b => b.ProductId == p.ProductId).Sum(b => b.Amount) > 0))
            .ToList();
        
        var outOfStockProducts = _mapper.Map<List<ProductDomian>>(outOfStockDbProducts);
        return outOfStockProducts;
    }


    public async Task<double> CalculateAverageProductPriceAsync()
    {
        return await _context.Products.AverageAsync(p => p.Price);
    }
}