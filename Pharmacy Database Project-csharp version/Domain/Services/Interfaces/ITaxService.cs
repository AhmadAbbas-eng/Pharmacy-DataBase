namespace Domain.Services.Interfaces;

public interface ITaxService
{
    Task<double> CalculateTaxForPeriodAsync(DateTime startDate, DateTime endDate);
}