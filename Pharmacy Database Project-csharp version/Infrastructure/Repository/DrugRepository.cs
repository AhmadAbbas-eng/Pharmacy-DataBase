using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class DrugRepository : IDrugRepository
{
    private readonly PharmacyDbContext _context;
    private readonly DrugMapper _mapper;

    public DrugRepository(PharmacyDbContext context, DrugMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<DrugDomain>> FindByRiskCategoryAsync(char riskCategory)
    {
        var drugs = await _context.Drugs
            .Where(d => d.DrugRiskPregnancyCategory == riskCategory)
            .ToListAsync();
        return drugs.Select(d => _mapper.MapToDomain(d));
    }

    public async Task<IEnumerable<DrugDomain>> GetAllAsync()
    {
        var drugs = await _context.Drugs.ToListAsync();
        return drugs.Select(d => _mapper.MapToDomain(d));
    }

    public async Task<DrugDomain?> GetByIdAsync(int id)
    {
        var drug = await _context.Drugs.FindAsync(id);
        return drug == null ? null : _mapper.MapToDomain(drug);
    }

    public async Task<DrugDomain> AddAsync(DrugDomain drug)
    {
        var entity = _mapper.MapToEntity(drug);
        _context.Drugs.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<DrugDomain?> UpdateAsync(DrugDomain drug)
    {
        var entity = await _context.Drugs.FindAsync(drug.ProductId);
        if (entity == null) return null;

        _mapper.MapToEntity(drug, entity);
        _context.Drugs.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(int id)
    {
        var entity = await _context.Drugs.FindAsync(id);
        if (entity == null) return false;

        _context.Drugs.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}