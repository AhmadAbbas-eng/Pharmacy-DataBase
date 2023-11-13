using System.ComponentModel.DataAnnotations;

namespace Infrastructure.Entities;

public class Drug : Product
{
    [Key] public int ProductId { get; set; }

    [Required] [StringLength(64)] public string DrugScientificName { get; set; }

    [Required]
    [StringLength(2)]
    [RegularExpression("A|B|C")]
    public string DrugRiskPregnancyCategory { get; set; }

    [StringLength(64)] public string? DrugDosage { get; set; }

    [StringLength(32)] public string? DrugCategory { get; set; }

    [StringLength(16)] public string? DrugDosageForm { get; set; }

    [StringLength(32)] public string? DrugPharmaceuticalCategory { get; set; }

    public Product Product { get; set; }
}