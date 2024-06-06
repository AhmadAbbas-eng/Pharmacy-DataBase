using Domain.Models;
using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ProductController : ControllerBase
{
    private readonly IProductService _productService;

    public ProductController(IProductService productService)
    {
        _productService = productService;
    }

    [HttpGet("total-amount/{productId}")]
    public async Task<IActionResult> GetTotalAmountById(int productId)
    {
        var totalAmount = await _productService.GetTotalAmountByIdAsync(productId);
        return Ok(totalAmount);
    }

    [HttpGet("out-of-stock")]
    public async Task<IActionResult> GetOutOfStock()
    {
        var outOfStockProducts = await _productService.GetOutOfStockAsync();
        return Ok(outOfStockProducts);
    }

    [HttpGet("average-price")]
    public async Task<IActionResult> CalculateAveragePrice()
    {
        var averagePrice = await _productService.CalculateAveragePriceAsync();
        return Ok(averagePrice);
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var products = await _productService.GetAllProductsAsync();
        return Ok(products);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var product = await _productService.GetProductByIdAsync(id);
        if (product == null) return NotFound();
        return Ok(product);
    }

    [HttpPost]
    public async Task<IActionResult> Add(ProductDomain product)
    {
        var addedProduct = await _productService.AddProductAsync(product);
        return CreatedAtAction(nameof(GetById), new { id = addedProduct.Id }, addedProduct);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, ProductDomain product)
    {
        if (id != product.Id) return BadRequest();

        var updatedProduct = await _productService.UpdateProductAsync(product);
        if (updatedProduct == null) return NotFound();
        return Ok(updatedProduct);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _productService.DeleteProductAsync(id);
        if (!deleted) return NotFound();
        return NoContent();
    }
}