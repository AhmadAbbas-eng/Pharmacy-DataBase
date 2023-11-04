using System.ComponentModel.DataAnnotations;

namespace Domain.Models;

public class DrugDomain : ProductDomain
{
    [Key] public int ProductId { get; set; }

    public string DrugScientificName { get; set; }

    public string DrugRiskPregnancyCategory { get; set; }

    public string? DrugDosage { get; set; }

    public string? DrugCategory { get; set; }

    public string? DrugDosageForm { get; set; }

    public string? DrugPharmaceuticalCategory { get; set; }
}