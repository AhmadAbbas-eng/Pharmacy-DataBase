namespace Domain.Exceptions;

public class EntityNotFoundException : Exception
{
    public string ErrorCode { get; set; }

    public EntityNotFoundException(string errorCode) : base()
    {
        ErrorCode = errorCode;
    }
}