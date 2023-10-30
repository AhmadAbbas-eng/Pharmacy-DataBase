using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

[Table("Employee")]
public class Employee
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int EmployeeId { get; set; }

    [Required]
    [StringLength(32)]
    public string Name { get; set; }

    [Required]
    [StringLength(16)]
    public string NationalId { get; set; }

    [Required]
    public DateTime DateOfWork { get; set; }

    [Required]
    [Range(0, double.MaxValue)]
    public double HourlyPaid { get; set; }

    [Required]
    [StringLength(64)]
    public string Password { get; set; }

    [StringLength(8)]
    public string IsManager { get; set; }

    [StringLength(8)] 
    public string IsActive { get; set; }

    public virtual ICollection<EmployeePhone> EmployeePhones { get; set; }
    public virtual ICollection<WorkHours> WorkHours { get; set; }
    public virtual ICollection<Cheque> Cheques { get; set; }
    public virtual ICollection<SupplierOrder> ManagedSupplierOrders { get; set; }
    public virtual ICollection<SupplierOrder> ReceivedSupplierOrders { get; set; }
    public virtual ICollection<EmployeeSalary> EmployeesSalaries { get; set; }
    public virtual ICollection<TaxesPayment> TaxesPayments { get; set; }
    public virtual ICollection<SupplierPayment> ManagedSupplierPayments { get; set; }
    public virtual ICollection<Income> Incomes { get; set; }
    public virtual ICollection<DrugDisposal> DrugDisposals { get; set; }
}
