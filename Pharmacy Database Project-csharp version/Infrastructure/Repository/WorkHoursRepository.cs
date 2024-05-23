using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;

namespace Infrastructure.Repository;

public class WorkHoursRepository : Repository<WorkHours, WorkHoursDomain, int>, IWorkHoursRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public WorkHoursRepository(PharmacyDbContext context, IMapper mapper) : base(context, mapper)
    {
        _context = context;
        _mapper = mapper;
    }
}