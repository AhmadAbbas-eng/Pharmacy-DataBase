using System.Text;
using Console_Application;
using Controllers;
using Microsoft.IdentityModel.Tokens;
using Serilog;

// Configure Serilog
Log.Logger = new LoggerConfiguration()
    .MinimumLevel.Debug()
    .WriteTo.Console()
    .WriteTo.File("logs/userinfo.txt", rollingInterval: RollingInterval.Day)
    .CreateLogger();

try
{
    var builder = WebApplication.CreateBuilder(args);

    builder.Host.UseSerilog();

    builder.Services.AddControllers();

    // app.MapGet("/", () => "Hello World!");
    var services = ServiceConfiguration.ConfigureServices(builder.Services);
    builder.Services.AddAuthentication("Bearer")
        .AddJwtBearer(options =>
            {
                options.TokenValidationParameters = new()
                {
                    ValidateIssuer = true,
                    ValidateAudience = true,
                    ValidateIssuerSigningKey = true,
                    ValidIssuer = builder.Configuration["Authentication:Issuer"],
                    ValidAudience = builder.Configuration["Authentication:Audience"],
                    IssuerSigningKey = new SymmetricSecurityKey(
                        Encoding.ASCII.GetBytes(builder.Configuration["Authentication:SecretForKey"]))
                };
            }
        );
    
    // builder.Services.AddAuthorization(options =>
    // {
    //     options.AddPolicy("MustBeFromAntwerp", policy =>
    //     {
    //         policy.RequireAuthenticatedUser();
    //         policy.RequireClaim("city", "Antwerp");
    //     });
    // });
    
    var app = builder.Build();

    app.UseRouting();
    
    
    app.UseAuthentication();
    app.UseAuthorization();
    
    app.UseEndpoints(endpoints =>
    {
        endpoints.MapControllers();
    });
    
    app.Run();
}
catch (Exception ex)
{
    Log.Fatal(ex, "An unhandled exception occurred during bootstrapping");
}
finally
{
    Log.CloseAndFlush();
}