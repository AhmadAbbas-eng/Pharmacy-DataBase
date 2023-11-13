﻿using AutoMapper;
using Domain.Models;
using Domain.Repositories.Interface;
using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure.Repository;

public class PaymentRepository : Repository<Payment, PaymentDomain, int>, IPaymentRepository
{
    private readonly PharmacyDbContext _context;
    private readonly IMapper _mapper;

    public PaymentRepository(PharmacyDbContext context, IMapper mapper) : base(context, mapper)
    {
        _context = context;
        _mapper = mapper;
    }

    public async Task<IEnumerable<PendingPayment>> GetPendingPaymentsAsync()
    {
        var thirtyDaysFromNow = DateTime.Now.AddDays(30);

        var pendingPayments = await _context.SupplierOrders
            .Include(o => o.Supplier)
            .Where(o => o.DueDateForPayment <= thirtyDaysFromNow)
            .ToListAsync();

        var pendingPayment = _mapper.Map<List<PendingPayment>>(pendingPayments);

        return pendingPayment;
    }
}