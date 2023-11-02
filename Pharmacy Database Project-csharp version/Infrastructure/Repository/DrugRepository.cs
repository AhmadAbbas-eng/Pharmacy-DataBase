using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class DrugRepository : IDrugRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public DrugRepository(PharmacyDbContext context, IMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<DrugDomain>> FindDrugsByRiskCategoryAsync(char riskCategory)
    {
        var drugs = await _context.Drugs
            .Where(d => d.DrugRiskPregnancyCategory.Contains(riskCategory))
            .ToListAsync();
        return _mapper.Map<List<DrugDomain>>(drugs);

    }
}
