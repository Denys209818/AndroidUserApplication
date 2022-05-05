using AndroidUserApplication.Data;
using AndroidUserApplication.Data.Identity.Entities;
using AndroidUserApplication.Mappers;
using AndroidUserApplication.Services;
using FluentValidation.AspNetCore;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Cors.Infrastructure;
using Microsoft.AspNetCore.HttpOverrides;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.FileProviders;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using Swashbuckle.AspNetCore.SwaggerGen;
using Swashbuckle.AspNetCore.SwaggerUI;
using System.Text;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

//  Ініціалізація контролерів і встановлення правило для конвертаї повертаючого обєкту (CamelCase)
builder.Services.AddControllers().AddNewtonsoftJson(opts => {

    opts.SerializerSettings.DefaultValueHandling = DefaultValueHandling.Include;
    opts.SerializerSettings.NullValueHandling = NullValueHandling.Ignore;
    opts.SerializerSettings.ContractResolver = new CamelCasePropertyNamesContractResolver();
    });
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
//  Встановлення Swagger
builder.Services.AddSwaggerGen((SwaggerGenOptions opts) => {
    opts.SwaggerDoc("v1", new OpenApiInfo { 
        Version = "v1",
        Title ="Web Api v1",
        Description = "Description fo this project"
    });
});

//  Встановлення підключення до БД
builder.Services.AddDbContext<EFAppContext>((DbContextOptionsBuilder builderOpts) => {
    builderOpts.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection"));
});

//  Встановлення налашутвань для авторизації Identity
builder.Services.AddIdentity<AppUser, AppRole>((IdentityOptions opts) => {
    opts.Password.RequireNonAlphanumeric = false;
    opts.Password.RequireDigit = false;
    opts.Password.RequireLowercase = false;
    opts.Password.RequireUppercase = false;
    opts.Password.RequiredLength = 6;
})
    .AddEntityFrameworkStores<EFAppContext>()
    .AddDefaultTokenProviders();

//  Встановлення налаштувань для авторизації за допомогою Jwt-токенів
builder.Services.AddAuthentication((AuthenticationOptions opts) => {
    opts.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    opts.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer((JwtBearerOptions jwtOpts) => { 
    jwtOpts.RequireHttpsMetadata = false;
    jwtOpts.SaveToken = true;
    jwtOpts.TokenValidationParameters = new TokenValidationParameters 
    {
        ValidateLifetime = false,
        ValidateIssuer=true,
        ValidateIssuerSigningKey = true,
        ValidateAudience = false,
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(
            builder.Configuration.GetValue<String>("private_key")))
    };
});
//  Додавання маппера
builder.Services.AddAutoMapper(typeof(IdentityMapper));
//  Додавання FluentValidation
builder.Services.AddFluentValidation(opts => opts
.RegisterValidatorsFromAssemblyContaining<Program>());
//  Додавання сервісу для генерації токенів
builder.Services.AddScoped<IJwtBearerService, JwtBearerService>();
//  Встновлення корсів для доступу до контроллерів
builder.Services.AddCors();


//Створення обєкту
var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    //Встановлення використання сваггера і вмикання сідера для заповення БД
    app.UseSwagger();
    app.UseSwaggerUI((SwaggerUIOptions opts) => {
        opts.SwaggerEndpoint("/swagger/v1/swagger.json", "My Web Api v1");
    });
}
app.NEW_SeedAll();
//Встановлення використвання авторизації
app.UseAuthentication();
app.UseAuthorization();

ForwardedHeadersOptions options = new ForwardedHeadersOptions { 
ForwardedHeaders = ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto
};

options.KnownNetworks.Clear();
options.KnownProxies.Clear();

app.UseForwardedHeaders(options);

//Задання правил потілики для корсів
app.UseCors((CorsPolicyBuilder builder) => {
    builder.AllowAnyHeader().AllowAnyHeader().AllowAnyOrigin();
});

//Формування статичної папки і надання до неї доступу
string dirRoot = Path.Combine(Directory.GetCurrentDirectory(), "Images");

if(!Directory.Exists(dirRoot))
    Directory.CreateDirectory(dirRoot);

app.UseStaticFiles(new StaticFileOptions { 
    FileProvider = new PhysicalFileProvider(dirRoot),
    RequestPath ="/images"
});

app.MapControllers();

app.Run();
