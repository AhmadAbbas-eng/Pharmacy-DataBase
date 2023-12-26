using System.Collections.Generic;
using System.Threading.Tasks;
using AutoFixture;
using Domain.Models;
using Domain.Repositories.Interface;
using Domain.Services.Implementations;
using Moq;

namespace Project_Test.ServicesUnitTest;

public class PaymentServiceTests
{
    private readonly Mock<IPaymentRepository> _mockRepository;
    private readonly PaymentService _paymentService;
    private readonly PaymentDomain _paymentDomain;
    private readonly List<BatchDomain> _paymentsBatch;
    private readonly Fixture _fixture;

    public PaymentServiceTests()
    {
        _mockRepository = new  Mock<IPaymentRepository>();
        _paymentService = new PaymentService(_mockRepository.Object);
        _fixture = new Fixture();

        _paymentDomain = _fixture.Build<PaymentDomain>().Create();
        _paymentsBatch = new List<BatchDomain>
        {
            _fixture.Build<BatchDomain>().Create(),
            _fixture.Build<BatchDomain>().Create()
        };

        _mockRepository.Setup(repo => repo.AddAllBatchesAsync(_paymentsBatch)).Returns(Task.CompletedTask);
        _mockRepository.Setup(repo => repo.SaveAsync())
            .Returns(Task.CompletedTask);
    }

    [Fact]
    public async Task ProcessPaymentsBatchAsync_CallsAddAndSave()
    {
        await _paymentService.ProcessPaymentsBatchAsync(_paymentsBatch);

        _mockRepository.Verify(repo => repo.AddAllBatchesAsync(_paymentsBatch), Times.Once);
    }
}
