using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class TaxRepository : ITaxRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public TaxRepository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<ICollection<TaxDomain>> FindByStartAndEndDateAsync(DateTime startDate, DateTime endDate)
    {
        var tax = await _context.Taxes
            .Where(x => x.TaxDate >= startDate && x.TaxDate <= endDate)
            .ToListAsync();
        return _mapper.Map<ICollection<TaxDomain>>(tax);
    }
}