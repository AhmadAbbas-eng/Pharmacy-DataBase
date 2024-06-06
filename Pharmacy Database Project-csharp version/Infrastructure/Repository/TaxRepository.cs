using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class TaxRepository : ITaxRepository
{
    private readonly PharmacyDbContext _context;
    private readonly TaxMapper _mapper;

    public TaxRepository(PharmacyDbContext context, TaxMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<ICollection<TaxDomain>> FindByStartAndEndDateAsync(DateTime startDate, DateTime endDate)
    {
        var tax = await _context.Taxes
            .Where(x => x.TaxDate >= startDate && x.TaxDate <= endDate)
            .ToListAsync();
        return tax.Select(x => _mapper.MapToDomain(x)).ToList();
    }

    public async Task<IEnumerable<TaxDomain>> GetAllAsync()
    {
        var taxes = await _context.Taxes.ToListAsync();
        return taxes.Select(t => _mapper.MapToDomain(t));
    }

    public async Task<TaxDomain?> GetByIdAsync(int id)
    {
        var tax = await _context.Taxes.FindAsync(id);
        return tax == null ? null : _mapper.MapToDomain(tax);
    }

    public async Task<TaxDomain> AddAsync(TaxDomain tax)
    {
        var entity = _mapper.MapToEntity(tax);
        _context.Taxes.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<TaxDomain?> UpdateAsync(TaxDomain tax)
    {
        var entity = await _context.Taxes.FindAsync(tax.Id);
        if (entity == null) return null;

        _mapper.MapToEntity(tax, entity);
        _context.Taxes.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.Taxes.FindAsync(id);
        if (entity == null) return false;

        _context.Taxes.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}