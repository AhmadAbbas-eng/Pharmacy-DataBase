using AutoFixture;
using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Implementations;
using Moq;

namespace Project_Test.ServicesUnitTest;

public class PaymentServiceTests
{
    private readonly Mock<IRepository<PaymentDomain, int>> _mockRepository;
    private readonly PaymentService _paymentService;
    private readonly List<PaymentDomain> _paymentsBatch;
    private readonly Fixture _fixture;

    public PaymentServiceTests()
    {
        _mockRepository = new Mock<IRepository<PaymentDomain, int>>();
        _paymentService = new PaymentService(_mockRepository.Object);
        _fixture = new Fixture();
        
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
        await _paymentService.ProcessPaymentsBatchAsync(_paymentsBatch);

        foreach (var payment in _paymentsBatch)
        {
            _mockRepository.Verify(repo => repo.AddAsync(payment), Times.Once);
        }
        _mockRepository.Verify(repo => repo.SaveAsync(), Times.Once);
    }
}
