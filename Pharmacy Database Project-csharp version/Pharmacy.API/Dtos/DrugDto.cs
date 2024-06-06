namespace Pharmacy.API.Dtos;

public class DrugDto
{
    public int ProductId { get; set; }
    public string DrugScientificName { get; set; }
    public string DrugRiskPregnancyCategory { get; set; }
    public string? DrugDosage { get; set; }
    public string? DrugCategory { get; set; }
    public string? DrugDosageForm { get; set; }
    public string? DrugPharmaceuticalCategory { get; set; }
}