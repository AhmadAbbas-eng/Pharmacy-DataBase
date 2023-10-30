using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Domain_Layer.Entities;

public class Product
{
    public Product()
    {
        Name = string.Empty;
        Manufacturer = string.Empty;
    }
    
    [Key] 
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)] 
    public int ProductId { get; set; }
    
    [Required(ErrorMessage = "The Name cannot be empty or null")]
    [StringLength(100, MinimumLength = 2, ErrorMessage = "Name must be between 2 and 100 characters.")]
    public string Name { get; set; }
    
    [Required(ErrorMessage = "Price is required.")]
    [Range(1, double.MaxValue, ErrorMessage = "Price must be non-zero")]
    public double Price { get; set; }
    
    [Required]
    [StringLength(100, MinimumLength = 2, ErrorMessage = "Manufacturer Name must be between 2 and 100 characters.")]
    public string Manufacturer { get; set; }
    
    [ForeignKey("Product_Name")]
    public NameManu NameManu { get; set; }
    public ICollection<Batch> Batches { get; set; }
    public Drug Drug { get; set; }
    public virtual ICollection<SupplierOrderBatch> SupplierOrderBatches { get; set; }
    public virtual ICollection<CustomerOrderBatch> CustomerOrderBatches { get; set; }
    public virtual ICollection<DrugDisposal> DrugDisposals { get; set; }

}