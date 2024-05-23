using System.Linq.Expressions;
using AutoFixture;
using AutoFixture.AutoMoq;
using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Implementations;
using Moq;

namespace Project_Test.ServicesUnitTest;

public class TaxServiceTests
{
    private readonly IFixture _fixture;
    private readonly Mock<IRepository<TaxDomain, string>> _mockRepository;
    private readonly List<TaxDomain> _taxesBatch;
    private readonly TaxService _taxService;

    public TaxServiceTests()
    {
        _fixture = new Fixture().Customize(new AutoMoqCustomization());
        _mockRepository = _fixture.Freeze<Mock<IRepository<TaxDomain, string>>>();
        _taxService = _fixture.Create<TaxService>();

        _taxesBatch = _fixture.Build<TaxDomain>()
            .With(t => t.TaxDate, _fixture.Create<DateTime>())
            .With(t => t.TaxValue, _fixture.Create<double>())
            .CreateMany(10)
            .ToList();

        _mockRepository.Setup(repo => repo.FindAsync(It.IsAny<Expression<Func<TaxDomain, bool>>>()))
            .ReturnsAsync(_taxesBatch);
    }

    [Fact]
    public async Task CalculateTaxForPeriodAsync_ReturnsCorrectSum()
    {
        var startDate = _taxesBatch.Min(t => t.TaxDate);
        var endDate = _taxesBatch.Max(t => t.TaxDate);

        var calculatedTax = await _taxService.CalculateTaxForPeriodAsync(startDate, endDate);

        var expectedSum = _taxesBatch.Sum(t => t.TaxValue);
        Assert.Equal(expectedSum, calculatedTax, 2);
    }
}