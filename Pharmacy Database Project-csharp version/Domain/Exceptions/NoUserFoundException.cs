namespace Domain.Exceptions;

public class NoUserFoundException : Exception
{
    public NoUserFoundException(string message) : base(message)
    {
        
    }
}