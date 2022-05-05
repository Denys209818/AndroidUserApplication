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

//  ����������� ���������� � ������������ ������� ��� �������� ������������ ����� (CamelCase)
builder.Services.AddControllers().AddNewtonsoftJson(opts => {

    opts.SerializerSettings.DefaultValueHandling = DefaultValueHandling.Include;
    opts.SerializerSettings.NullValueHandling = NullValueHandling.Ignore;
    opts.SerializerSettings.ContractResolver = new CamelCasePropertyNamesContractResolver();
    });
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
//  ������������ Swagger
builder.Services.AddSwaggerGen((SwaggerGenOptions opts) => {
    opts.SwaggerDoc("v1", new OpenApiInfo { 
        Version = "v1",
        Title ="Web Api v1",
        Description = "Description fo this project"
    });
});

//  ������������ ���������� �� ��
builder.Services.AddDbContext<EFAppContext>((DbContextOptionsBuilder builderOpts) => {
    builderOpts.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection"));
});

//  ������������ ����������� ��� ����������� Identity
builder.Services.AddIdentity<AppUser, AppRole>((IdentityOptions opts) => {
    opts.Password.RequireNonAlphanumeric = false;
    opts.Password.RequireDigit = false;
    opts.Password.RequireLowercase = false;
    opts.Password.RequireUppercase = false;
    opts.Password.RequiredLength = 6;
})
    .AddEntityFrameworkStores<EFAppContext>()
    .AddDefaultTokenProviders();

//  ������������ ����������� ��� ����������� �� ��������� Jwt-������
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
//  ��������� �������
builder.Services.AddAutoMapper(typeof(IdentityMapper));
//  ��������� FluentValidation
builder.Services.AddFluentValidation(opts => opts
.RegisterValidatorsFromAssemblyContaining<Program>());
//  ��������� ������ ��� ��������� ������
builder.Services.AddScoped<IJwtBearerService, JwtBearerService>();
//  ����������� ����� ��� ������� �� �����������
builder.Services.AddCors();


//��������� �����
var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    //������������ ������������ �������� � �������� ����� ��� ��������� ��
    app.UseSwagger();
    app.UseSwaggerUI((SwaggerUIOptions opts) => {
        opts.SwaggerEndpoint("/swagger/v1/swagger.json", "My Web Api v1");
    });
}
app.NEW_SeedAll();
//������������ ������������� �����������
app.UseAuthentication();
app.UseAuthorization();

ForwardedHeadersOptions options = new ForwardedHeadersOptions { 
ForwardedHeaders = ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto
};

options.KnownNetworks.Clear();
options.KnownProxies.Clear();

app.UseForwardedHeaders(options);

//������� ������ ������� ��� �����
app.UseCors((CorsPolicyBuilder builder) => {
    builder.AllowAnyHeader().AllowAnyHeader().AllowAnyOrigin();
});

//���������� �������� ����� � ������� �� �� �������
string dirRoot = Path.Combine(Directory.GetCurrentDirectory(), "Images");

if(!Directory.Exists(dirRoot))
    Directory.CreateDirectory(dirRoot);

app.UseStaticFiles(new StaticFileOptions { 
    FileProvider = new PhysicalFileProvider(dirRoot),
    RequestPath ="/images"
});

app.MapControllers();

app.Run();
