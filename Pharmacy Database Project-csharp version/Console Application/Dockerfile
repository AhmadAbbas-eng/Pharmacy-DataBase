FROM mcr.microsoft.com/dotnet/runtime:6.0 AS base
WORKDIR /app

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /src
COPY ["Console Application/Console Application.csproj", "Console Application/"]
RUN dotnet restore "Console Application/Console Application.csproj"
COPY . .
WORKDIR "/src/Console Application"
RUN dotnet build "Console Application.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "Console Application.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "Console Application.dll"]
