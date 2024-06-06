namespace Pharmacy.API.Dtos;

public class CustomerDto
{
    public int Id { get; set; }
    public string CustomerNID { get; set; }
    public string Name { get; set; }
    public double Debt { get; set; }
}