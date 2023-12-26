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


var builder = WebApplication.CreateBuilder(args);

builder.Host.UseSerilog();

builder.Services.AddControllers().AddNewtonsoftJson();

builder.Services.AddConnectionStringConfiguration(builder.Configuration);
builder.Services.AddDatabaseConfiguration();
builder.Services.AddLoggerConfiguration();
builder.Services.AddApplicationServices();
builder.Services.AddAutoMapper();
builder.Services.AddMapperly();
builder.Services.AddJwtAuthentication(builder.Configuration);
builder.Services.AddSwaggerConfiguration();

// builder.Services.AddAuthorization(options =>
// {
//     options.AddPolicy("MustBeFromAntwerp", policy =>
//     {
//         policy.RequireAuthenticatedUser();
//         policy.RequireClaim("city", "Antwerp");
//     });
// });

var app = builder.Build();


if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "Your API V1"));
}


app.UseRouting();


// app.UseAuthentication();
// app.UseAuthorization();

app.UseEndpoints(endpoints =>
{
    endpoints.MapControllers();
});

try{   
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