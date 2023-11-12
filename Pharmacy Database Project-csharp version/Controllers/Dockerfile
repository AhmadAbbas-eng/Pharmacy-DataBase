FROM mcr.microsoft.com/dotnet/aspnet:6.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /src
COPY ["Controllers/Controllers.csproj", "Controllers/"]
RUN dotnet restore "Controllers/Controllers.csproj"
COPY . .
WORKDIR "/src/Controllers"
RUN dotnet build "Controllers.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "Controllers.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Controllers.dll"]
