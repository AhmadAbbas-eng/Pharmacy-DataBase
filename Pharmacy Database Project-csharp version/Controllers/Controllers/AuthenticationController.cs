using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Console_Application.Model.Messages;
using Domain.Models;
using Domain.Services.Interfaces;
using Infrastructure.Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;

namespace Controllers.Controllers;

[Route("api/authentication")]
[ApiController]
public class AuthenticationController : ControllerBase
{
    private readonly IConfiguration _configuration;
    private readonly IEmployeeService _employeeService;
    private readonly ILogger<AuthenticationController> _logger;

    public AuthenticationController(IConfiguration configuration, IEmployeeService employeeService, ILogger<AuthenticationController> logger)
    {
        _configuration = configuration;
        _employeeService = employeeService;
        _logger = logger;
    }
    
    
    [HttpPost("authenticate")]
    public ActionResult<string> Authenticate(
        AuthenticationRequestBody authenticationRequestBody)
    {
        
        var user = ValidateUserCredentials(
            authenticationRequestBody.UserName,
            authenticationRequestBody.Password);

        if (user == null) return Unauthorized();

        var securityKey = new SymmetricSecurityKey(
            Encoding.ASCII.GetBytes(_configuration["Authentication:SecretForKey"]));
        var signingCredentials = new SigningCredentials(
            securityKey, SecurityAlgorithms.HmacSha256);

        var claimsForToken = new List<Claim>();
        claimsForToken.Add(new Claim("sub", user.UserId.ToString()));
        claimsForToken.Add(new Claim("user_name", user.UserName));
        claimsForToken.Add(new Claim("is_manager", user.IsManager));

        var jwtSecurityToken = new JwtSecurityToken(
            _configuration["Authentication:Issuer"],
            _configuration["Authentication:Audience"],
            claimsForToken,
            DateTime.UtcNow,
            DateTime.UtcNow.AddHours(1),
            signingCredentials);

        var tokenToReturn = new JwtSecurityTokenHandler()
            .WriteToken(jwtSecurityToken);

        return Ok(tokenToReturn);
    }

    private UserInfo? ValidateUserCredentials(string userName, string password)
    {
        return new UserInfo(1, "Ahmad", "Ahmad");
        try
        {
            var user = _employeeService.ValidatePassword(userName, password);

            var userInfo = new UserInfo
            (
                user.EmployeeId,
                user.Name,
                user.IsManager
            );

            return userInfo;
        }
        catch (Exception exception)
        {
            _logger.LogError(exception.Message);
            return null;
        }
    }

    private class UserInfo
    {
        public UserInfo(
            int userId,
            string userName,
            string isManager)
        {
            UserId = userId;
            UserName = userName;
            IsManager = isManager;
        }

        public int UserId { get; }
        public string UserName { get; set; }
        public string IsManager { get; }
    }
}