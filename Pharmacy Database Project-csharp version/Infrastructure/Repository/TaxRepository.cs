using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;

namespace Infrastructure.Repository;

public class TaxRepository : Repository<Tax, TaxDomain, string>, ITaxRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public TaxRepository(PharmacyDbContext context, IMapper mapper) : base(context, mapper)
    {
        _context = context;
        _mapper = mapper;
    }

}