using System.Linq.Expressions;
using AutoFixture;
using AutoFixture.AutoMoq;
using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Implementations;
using Moq;

namespace Project_Test.ServicesUnitTest;

public class WorkHoursServiceTests
{
    private readonly IFixture _fixture;
    private readonly Mock<IRepository<WorkHoursDomain, int>> _mockRepository;
    private readonly List<WorkHoursDomain> _workHoursBatch;
    private readonly WorkHoursService _workHoursService;

    public WorkHoursServiceTests()
    {
        _fixture = new Fixture().Customize(new AutoMoqCustomization());

        _mockRepository = _fixture.Freeze<Mock<IRepository<WorkHoursDomain, int>>>();
        _workHoursService = _fixture.Create<WorkHoursService>();

        var employeeId = 1;
        var month = 5;
        var year = 2023;

        _workHoursBatch = _fixture.Build<WorkHoursDomain>()
            .With(wh => wh.EmployeeId, employeeId)
            .With(wh => wh.WorkedMonth, month)
            .With(wh => wh.WorkedYear, year)
            .With(wh => wh.HourlyPaid, _fixture.Create<double>())
            .With(wh => wh.WorkedHours, _fixture.Create<int>())
            .CreateMany(10)
            .ToList();

        _mockRepository.Setup(repo => repo.FindAsync(It.IsAny<Expression<Func<WorkHoursDomain, bool>>>()))
            .ReturnsAsync(_workHoursBatch);
    }

    [Fact]
    public async Task CalculateMonthlyWagesAsync_ReturnsCorrectSum()
    {
        var employeeId = 1;
        var month = 5;
        var year = 2023;

        var wages = await _workHoursService.CalculateMonthlyWagesAsync(employeeId, month, year);

        var expectedWages = _workHoursBatch.Sum(wh => wh.WorkedHours * wh.HourlyPaid);
        Assert.Equal(expectedWages, wages);
    }

    [Fact]
    public async Task GenerateMonthlyWorkReportsAsync_ReturnsAllRecordsForMonthAndYear()
    {
        var month = 5;
        var year = 2023;

        var reports = await _workHoursService.GenerateMonthlyWorkReportsAsync(month, year);

        Assert.Equal(_workHoursBatch.Count, reports.Count());
        Assert.All(reports, item => Assert.Contains(_workHoursBatch, batchItem => batchItem == item));
    }
}