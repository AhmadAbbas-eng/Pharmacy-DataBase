using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class Payment
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int PaymentId { get; set; }

    [Required]
    public DateTime PaymentDate { get; set; }

    [Required]
    [Range(0, double.MaxValue)]
    public double PaymentAmount { get; set; }

    [Required]
    [StringLength(16)]
    public string PaymentMethod { get; set; }

    public virtual ICollection<Cheque> Cheques { get; set; }
    public virtual ICollection<EmployeeSalary> EmployeesSalaries { get; set; }
    public virtual ICollection<TaxesPayment> TaxesPayments { get; set; }
    public virtual ICollection<SupplierPayment> SupplierPayments { get; set; }
    public virtual ICollection<DrugDisposal> DrugDisposals { get; set; }
}
