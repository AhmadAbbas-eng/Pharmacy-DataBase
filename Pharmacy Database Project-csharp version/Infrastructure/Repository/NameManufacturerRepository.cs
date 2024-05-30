using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class NameManufacturerRepository : INameManufacturerRepository
{
    private readonly PharmacyDbContext _context;
    private readonly NameManufacturerMapper _mapper;

    public NameManufacturerRepository(PharmacyDbContext context, NameManufacturerMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<string>> GetDistinctAsync()
    {
        return await _context.NameMenus
            .Select(nm => nm.ProductManufacturer)
            .Distinct()
            .ToListAsync();
    }

    public async Task UpdateAsync(string oldName, string newName)
    {
        var productsToUpdate = await _context.NameMenus
            .Where(nm => nm.ProductManufacturer == oldName)
            .ToListAsync();

        productsToUpdate.ForEach(nm => { nm.ProductManufacturer = newName; });
        await _context.SaveChangesAsync();
    }

    public async Task<IEnumerable<NameManufacturerDomain>> GetAllAsync()
    {
        var nameManufacturers = await _context.NameMenus.ToListAsync();
        return nameManufacturers.Select(nm => _mapper.MapToDomain(nm));
    }

    public async Task<NameManufacturerDomain?> GetByIdAsync(int id)
    {
        var nameManufacturer = await _context.NameMenus.FindAsync(id);
        return nameManufacturer == null ? null : _mapper.MapToDomain(nameManufacturer);
    }

    public async Task<NameManufacturerDomain> AddAsync(NameManufacturerDomain nameManufacturer)
    {
        var entity = _mapper.MapToEntity(nameManufacturer);
        _context.NameMenus.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<NameManufacturerDomain?> UpdateAsync(NameManufacturerDomain nameManufacturer)
    {
        var entity = await _context.NameMenus.FindAsync(nameManufacturer.ProductId);
        if (entity == null) return null;

        _mapper.MapToEntity(nameManufacturer, entity);
        _context.NameMenus.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.NameMenus.FindAsync(id);
        if (entity == null) return false;

        _context.NameMenus.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}