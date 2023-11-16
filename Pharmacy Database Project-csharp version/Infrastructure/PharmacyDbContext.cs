using Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;

namespace Infrastructure;

public class PharmacyDbContext : DbContext
{
    public PharmacyDbContext()
    {
    }

    public PharmacyDbContext(DbContextOptions<PharmacyDbContext> options)
        : base(options)
    {
    }

    public DbSet<Batch> Batches { get; set; }
    public DbSet<Customer> Customers { get; set; }
    public DbSet<CustomerOrder> CustomerOrders { get; set; }
    public DbSet<Drug> Drugs { get; set; }
    public DbSet<Employee> Employees { get; set; }
    public DbSet<EmployeeSalary> EmployeeSalaries { get; set; }
    public DbSet<NameManufacturer> NameMenus { get; set; }
    public DbSet<Product> Products { get; set; }
    public DbSet<SupplierOrderBatch> SupplierOrderBatches { get; set; }
    public DbSet<SupplierPayment> SupplierPayments { get; set; }
    public DbSet<Tax> Taxes { get; set; }
    public DbSet<WorkHours> WorkHours { get; set; }
    public DbSet<Cheque> Cheques { get; set; }
    public DbSet<CustomerOrderBatch> CustomerOrderBatches { get; set; }
    public DbSet<CustomerPhone> CustomerPhones { get; set; }
    public DbSet<DrugDisposal> DrugDisposals { get; set; }
    public DbSet<EmployeePhone> EmployeePhones { get; set; }
    public DbSet<Income> Incomes { get; set; }
    public DbSet<Payment> Payments { get; set; }
    public DbSet<Supplier> Suppliers { get; set; }
    public DbSet<SupplierOrder> SupplierOrders { get; set; }
    public DbSet<SupplierPhone> SupplierPhones { get; set; }
    public DbSet<TaxesPayment> TaxesPayments { get; set; }

    
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<NameManufacturer>(
            nm => { });

        modelBuilder.Entity<Product>(
            product =>
            {
                product.HasOne<NameManufacturer>(p => p.NameManufacturer)
                    .WithOne(nm => nm.Product)
                    .HasForeignKey<NameManufacturer>(nm => nm.ProductId)
                    .OnDelete(DeleteBehavior.Cascade);

                product.HasMany<Batch>(p => p.Batches)
                    .WithOne(b => b.Product)
                    .HasForeignKey(b => b.ProductId)
                    .OnDelete(DeleteBehavior.NoAction);

                product.HasOne<Drug>(p => p.Drug)
                    .WithOne(d => d.Product)
                    .HasForeignKey<Drug>(d => d.ProductId);
            });

        modelBuilder.Entity<Supplier>(
            supplier =>
            {
                supplier.HasMany<SupplierOrder>(s => s.SupplierOrders)
                    .WithOne(so => so.Supplier)
                    .HasForeignKey(so => so.SupplierId)
                    .OnDelete(DeleteBehavior.NoAction);

                supplier.HasMany<SupplierPhone>(s => s.SupplierPhones)
                    .WithOne(so => so.Supplier)
                    .HasForeignKey(so => so.SupplierId)
                    .OnDelete(DeleteBehavior.Cascade);


                supplier.HasMany<SupplierPayment>(s => s.SupplierPayments)
                    .WithOne(so => so.Supplier)
                    .HasForeignKey(so => so.SupplierId)
                    .OnDelete(DeleteBehavior.NoAction);
            });

        modelBuilder.Entity<Batch>(
            batch =>
            {
                batch.HasIndex(b => new { b.ProductId, b.ProductionDate, b.ExpiryDate });
                batch.HasMany<SupplierOrderBatch>(b => b.SupplierOrderBatches)
                    .WithOne(sob => sob.Batch)
                    .HasForeignKey(sob => sob.BatchId)
                    .OnDelete(DeleteBehavior.NoAction);

                batch.HasMany<CustomerOrderBatch>(b => b.CustomerOrderBatches)
                    .WithOne(cob => cob.Batch)
                    .HasForeignKey(sob => sob.BatchId)
                    .OnDelete(DeleteBehavior.NoAction);

                batch.HasMany<DrugDisposal>(b => b.DrugDisposals)
                    .WithOne(dd => dd.Batch)
                    .HasForeignKey(dd => dd.BatchId)
                    .OnDelete(DeleteBehavior.NoAction);
            });

        modelBuilder.Entity<Customer>(customer =>
        {
            customer.HasMany<CustomerOrder>(c => c.CustomerOrders)
                .WithOne(co => co.Customer)
                .HasForeignKey(co => co.CustomerId)
                .OnDelete(DeleteBehavior.NoAction);

            customer.HasMany<CustomerPhone>(c => c.CustomerPhones)
                .WithOne(cp => cp.Customer)
                .HasForeignKey(cp => cp.CustomerId)
                .OnDelete(DeleteBehavior.Cascade);

            customer.HasMany<Income>(c => c.Incomes)
                .WithOne(i => i.Customer)
                .HasForeignKey(i => i.CustomerId)
                .OnDelete(DeleteBehavior.NoAction);
        });

        modelBuilder.Entity<CustomerOrder>(
            customerOrder =>
            {
                customerOrder.HasMany<CustomerOrderBatch>(c => c.CustomerOrderBatches)
                    .WithOne(cob => cob.CustomerOrder)
                    .HasForeignKey(cob => cob.OrderId)
                    .OnDelete(DeleteBehavior.NoAction);
            });

        modelBuilder.Entity<SupplierOrder>(
            customerOrder =>
            {
                customerOrder.HasMany<SupplierOrderBatch>(s => s.SupplierOrderBatches)
                    .WithOne(sob => sob.SupplierOrder)
                    .HasForeignKey(sob => sob.OrderId)
                    .OnDelete(DeleteBehavior.NoAction);
            });

        modelBuilder.Entity<Employee>(employee =>
        {
            employee.HasMany<EmployeePhone>(e => e.EmployeePhones)
                .WithOne(ep => ep.Employee)
                .HasForeignKey(ep => ep.EmployeeId)
                .OnDelete(DeleteBehavior.Cascade);

            employee.HasMany<WorkHours>(e => e.WorkHours)
                .WithOne(wh => wh.Employee)
                .HasForeignKey(wh => wh.EmployeeId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<Cheque>(e => e.Cheques)
                .WithOne(c => c.Manager)
                .HasForeignKey(c => c.ManagerId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<CustomerOrder>(e => e.CustomerOrders)
                .WithOne(co => co.Employee)
                .HasForeignKey(co => co.EmployeeId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<SupplierOrder>(e => e.ManagedSupplierOrders)
                .WithOne(so => so.Manager)
                .HasForeignKey(so => so.ManagerId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<SupplierOrder>(e => e.ReceivedSupplierOrders)
                .WithOne(so => so.Receiver)
                .HasForeignKey(so => so.ReceiverId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<EmployeeSalary>(e => e.ReceiverEmployeeSalaries)
                .WithOne(es => es.Employee)
                .HasForeignKey(es => es.EmployeeId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<EmployeeSalary>(e => e.PayeeEmployeeSalaries)
                .WithOne(es => es.Manager)
                .HasForeignKey(es => es.ManagerId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<TaxesPayment>(e => e.TaxesPayments)
                .WithOne(tp => tp.Manager)
                .HasForeignKey(wh => wh.ManagerId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<SupplierPayment>(e => e.ManagedSupplierPayments)
                .WithOne(sp => sp.Manager)
                .HasForeignKey(sp => sp.ManagerId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<Income>(e => e.Incomes)
                .WithOne(i => i.Employee)
                .HasForeignKey(i => i.EmployeeId)
                .OnDelete(DeleteBehavior.NoAction);

            employee.HasMany<DrugDisposal>(e => e.DrugDisposals)
                .WithOne(dd => dd.Employee)
                .HasForeignKey(dd => dd.EmployeeId)
                .OnDelete(DeleteBehavior.NoAction);
        });

        modelBuilder.Entity<Payment>(payment =>
        {
            payment.HasMany<Cheque>(p => p.Cheques)
                .WithOne(c => c.Payment)
                .HasForeignKey(c => c.PaymentId)
                .OnDelete(DeleteBehavior.NoAction);

            payment.HasMany<EmployeeSalary>(p => p.EmployeesSalaries)
                .WithOne(e => e.Payment)
                .HasForeignKey(e => e.PaymentId)
                .OnDelete(DeleteBehavior.NoAction);

            payment.HasMany<TaxesPayment>(p => p.TaxesPayments)
                .WithOne(tp => tp.Payment)
                .HasForeignKey(tp => tp.PaymentId)
                .OnDelete(DeleteBehavior.NoAction);

            payment.HasMany<SupplierPayment>(p => p.SupplierPayments)
                .WithOne(sp => sp.Payment)
                .HasForeignKey(sp => sp.PaymentId)
                .OnDelete(DeleteBehavior.NoAction);

            payment.HasMany<DrugDisposal>(p => p.DrugDisposals)
                .WithOne(dd => dd.Payment)
                .HasForeignKey(dd => dd.PaymentId)
                .OnDelete(DeleteBehavior.NoAction);
        });

        modelBuilder.Entity<Tax>(tax =>
        {
            tax.Property(t => t.TaxId).ValueGeneratedOnAdd();

            tax.HasMany<TaxesPayment>(t => t.TaxesPayments)
                .WithOne(tp => tp.Tax)
                .HasForeignKey(tp => tp.TaxId)
                .OnDelete(DeleteBehavior.NoAction);
        });

        modelBuilder.Entity<TaxesPayment>(taxesPayment =>
        {
            taxesPayment.HasKey(tp => new { tp.PaymentId, tp.ManagerId, tp.TaxId });
        });

        modelBuilder.Entity<CustomerOrderBatch>(cob => { cob.HasKey(c => new { c.OrderId, c.BatchId }); });

        modelBuilder.Entity<SupplierOrderBatch>(sob => { sob.HasKey(s => new { s.OrderId, s.BatchId }); });

        modelBuilder.Entity<WorkHours>(workHours =>
        {
            workHours.HasKey(w => new { w.EmployeeId, w.WorkedMonth, w.WorkedYear });
        });

        modelBuilder.Entity<EmployeePhone>(employeePhone =>
        {
            employeePhone.HasKey(e => new { e.EmployeeId, e.Phone });
        });

        modelBuilder.Entity<CustomerPhone>(customerPhone =>
        {
            customerPhone.HasKey(e => new { e.CustomerId, e.Phone });
        });

        modelBuilder.Entity<SupplierPhone>(supplierPhone =>
        {
            supplierPhone.HasKey(s => new { s.SupplierId, s.Phone });
        });

        modelBuilder.Entity<EmployeeSalary>(empSalary =>
        {
            empSalary.HasKey(es => new { es.PaymentId, es.ManagerId, es.EmployeeId });
        });

        modelBuilder.Entity<SupplierPayment>(sp => { sp.HasKey(s => new { s.SupplierId, s.PaymentId, s.ManagerId }); });
    }
}