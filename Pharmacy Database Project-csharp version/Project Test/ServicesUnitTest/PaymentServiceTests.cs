using AutoFixture;
using AutoFixture.AutoMoq;
using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Implementations;
using Moq;

namespace Project_Test.ServicesUnitTest;

public class PaymentServiceTests
{
    private readonly IFixture _fixture;
    private readonly Mock<IRepository<PaymentDomain, int>> _mockRepository;
    private readonly List<PaymentDomain> _paymentsBatch;
    private readonly PaymentService _paymentService;

    public PaymentServiceTests()
    {
        _fixture = new Fixture().Customize(new AutoMoqCustomization());
        _mockRepository = _fixture.Freeze<Mock<IRepository<PaymentDomain, int>>>();
        _paymentService = _fixture.Create<PaymentService>();

        _paymentsBatch = new List<PaymentDomain>
        {
            _fixture.Build<PaymentDomain>().Create(),
            _fixture.Build<PaymentDomain>().Create()
        };

        _mockRepository.Setup(repo => repo.AddAsync(_paymentsBatch[0]))
            .Returns(Task.FromResult(_paymentsBatch[0].PaymentId));

        _mockRepository.Setup(repo => repo.AddAsync(_paymentsBatch[1]))
            .Returns(Task.FromResult(_paymentsBatch[1].PaymentId));

        _mockRepository.Setup(repo => repo.SaveAsync())
            .Returns(Task.CompletedTask);
    }

    [Fact]
    public async Task ProcessPaymentsBatchAsync_CallsAddAndSave()
    {
        // await _paymentService.ProcessPaymentsBatchAsync(_paymentsBatch);
        //
        // foreach (var payment in _paymentsBatch)
        // {
        //     _mockRepository.Verify(repo => repo.AddAsync(payment), Times.Once);
        // }
        // _mockRepository.Verify(repo => repo.SaveAsync(), Times.Once);
    }
}