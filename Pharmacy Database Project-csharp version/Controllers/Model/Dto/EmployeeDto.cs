namespace Controllers.Model.Dto;

public class EmployeeDto
{
    public int EmployeeId { get; set; }
    public string Name { get; set; }
    public string NationalId { get; set; }
    public string Password { get; set; }
    public double HourlyPaid { get; set; } 
    public  DateTime DateOfWork { get; set; }
    
    public string IsManager { get; set; }

    public string IsActive { get; set; }
}