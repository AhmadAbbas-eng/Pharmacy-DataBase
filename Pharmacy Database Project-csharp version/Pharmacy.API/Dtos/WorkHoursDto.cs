namespace Pharmacy.API.Dtos;

public class WorkHoursDto
{
    public int Id { get; set; }
    public int EmployeeId { get; set; }
    public int Month { get; set; }
    public int Year { get; set; }
    public int Hours { get; set; }
}