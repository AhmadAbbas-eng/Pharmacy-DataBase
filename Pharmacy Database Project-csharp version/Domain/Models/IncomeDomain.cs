namespace Domain.Models;
public class IncomeDomain
{
    public int IncomeId { get; set; }
    public int Amount { get; set; }
    public DateTime IncomeDate { get; set; }
    public int EmployeeId { get; set; }
    public int CustomerId { get; set; }
}