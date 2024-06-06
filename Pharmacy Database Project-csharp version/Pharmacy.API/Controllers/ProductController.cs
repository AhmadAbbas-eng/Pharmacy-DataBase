using Domain.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Pharmacy.API.Dtos;
using Pharmacy.API.Mappers;

namespace Pharmacy.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ProductController : ControllerBase
{
    private readonly ProductMapper _productMapper;
    private readonly IProductService _productService;

    public ProductController(IProductService productService, ProductMapper productMapper)
    {
        _productService = productService;
        _productMapper = productMapper;
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
        var outOfStockProductDtos = outOfStockProducts.Select(_productMapper.ToDto);
        return Ok(outOfStockProductDtos);
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
        var productDtos = products.Select(_productMapper.ToDto);
        return Ok(productDtos);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
        var product = await _productService.GetProductByIdAsync(id);
        if (product == null) return NotFound();

        var productDto = _productMapper.ToDto(product);
        return Ok(productDto);
    }

    [HttpPost]
    public async Task<IActionResult> Add(ProductDto productDto)
    {
        var productDomain = _productMapper.ToDomain(productDto);
        var addedProduct = await _productService.AddProductAsync(productDomain);
        var addedProductDto = _productMapper.ToDto(addedProduct);
        return CreatedAtAction(nameof(GetById), new { id = addedProductDto.Id }, addedProductDto);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, ProductDto productDto)
    {
        if (id != productDto.Id) return BadRequest();

        var productDomain = _productMapper.ToDomain(productDto);
        var updatedProduct = await _productService.UpdateProductAsync(productDomain);
        if (updatedProduct == null) return NotFound();

        var updatedProductDto = _productMapper.ToDto(updatedProduct);
        return Ok(updatedProductDto);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        var deleted = await _productService.DeleteProductAsync(id);
        if (!deleted) return NotFound();

        return NoContent();
    }
}