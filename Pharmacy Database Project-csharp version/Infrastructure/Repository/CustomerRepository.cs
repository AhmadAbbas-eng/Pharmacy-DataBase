using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Mappers;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class CustomerRepository : ICustomerRepository
{
    private readonly PharmacyDbContext _context;
    private readonly CustomerMapper _mapper;

    public CustomerRepository(PharmacyDbContext context, CustomerMapper mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public Task<double> CalculateTotalDebtByIdAsync(string customerId)
    {
        return _context.Customers
            .Where(c => c.CustomerNID == customerId)
            .Select(c => c.Debt)
            .SumAsync();
    }

    public async Task<IEnumerable<CustomerDomain>> FindWithOrdersAsync()
    {
        var customers = await _context.Customers
            .Where(c => c.CustomerOrders.Any())
            .ToListAsync();
        return customers.Select(c => _mapper.MapToDomain(c));
    }

    public async Task UpdatePhoneAsync(string oldPhoneNumber, string newPhoneNumber)
    {
        var customerPhone = await _context.CustomerPhones
            .FirstOrDefaultAsync(cp => cp.Phone == oldPhoneNumber);

        if (customerPhone != null)
        {
            customerPhone.Phone = newPhoneNumber;
            await _context.SaveChangesAsync();
        }
    }

    public async Task<IEnumerable<CustomerDomain>> GetAllAsync()
    {
        var customers = await _context.Customers.ToListAsync();
        return customers.Select(c => _mapper.MapToDomain(c));
    }

    public async Task<CustomerDomain?> GetByIdAsync(string customerId)
    {
        var customer = await _context.Customers.FindAsync(customerId);
        return customer == null ? null : _mapper.MapToDomain(customer);
    }

    public async Task<CustomerDomain> AddAsync(CustomerDomain customer)
    {
        var entity = _mapper.MapToEntity(customer);
        _context.Customers.Add(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<CustomerDomain?> UpdateAsync(CustomerDomain customer)
    {
        var entity = await _context.Customers.FindAsync(customer.CustomerNID);
        if (entity == null) return null;

        _mapper.MapToEntity(customer, entity);
        _context.Customers.Update(entity);
        await _context.SaveChangesAsync();
        return _mapper.MapToDomain(entity);
    }

    public async Task<bool> DeleteAsync(string customerId)
    {
        var entity = await _context.Customers.FindAsync(customerId);
        if (entity == null) return false;

        _context.Customers.Remove(entity);
        await _context.SaveChangesAsync();
        return true;
    }
}