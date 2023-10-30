using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

[Table("Drug")]
public class Drug
{
    [Key]
    [ForeignKey("Product")]
    public int ProductId { get; set; }

    [Required]
    [StringLength(64)]
    public string DrugScientificName { get; set; }

    [Required]
    [StringLength(2)]
    [RegularExpression("A|B|C")]
    public string DrugRiskPregnancyCategory { get; set; }

    [StringLength(64)]
    public string? DrugDosage { get; set; } 

    [StringLength(32)]
    public string? DrugCategory { get; set; }

    [StringLength(16)]
    public string? DrugDosageForm { get; set; } 

    [StringLength(32)]
    public string? DrugPharmaceuticalCategory { get; set; }

    public Product Product { get; set; }
}

