using AndroidUserApplication.Constants;
using AndroidUserApplication.Data;
using AndroidUserApplication.Data.Identity.Entities;
using Bogus;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;

namespace AndroidUserApplication.Services
{
    public static class DbSeeder
    {

        public static void SeedAll(this IApplicationBuilder app) 
        {
            //Отримання провайдера для отримування системних сервісів
            using (var scope = app.ApplicationServices.GetRequiredService<IServiceScopeFactory>().CreateScope()) 
            {
                //Отримування менеджера для ролей
                var roleManager = scope.ServiceProvider.GetRequiredService<RoleManager<AppRole>>();

                //Перевірка чи таблиця ролей не пуста
                if (!roleManager.Roles.Any()) 
                {
                    //Створення ролей
                    var res = roleManager.CreateAsync(new AppRole {
                        Name = Roles.USER
                    }).Result;

                    res = roleManager.CreateAsync(new AppRole
                    {
                        Name = Roles.ADMIN
                    }).Result;
                }
            }
        }

        public static void NEW_SeedAll(this WebApplication app) 
        {
            using (var scope = app.Services.CreateScope()) 
            {
                var context = scope.ServiceProvider.GetRequiredService<EFAppContext>();

                context.Database.Migrate();
                GenerateUser(scope.ServiceProvider);
                GenerateRole(scope.ServiceProvider);


            }
        }

        private static void GenerateUser(IServiceProvider provider) 
        {
            var userManager = provider.GetRequiredService<UserManager<AppUser>>();
            if (!userManager.Users.Any()) 
            {
                Faker<AppUser> faker = new Faker<AppUser>("uk")
                    .RuleFor(x => x.FirstName, x => x.Name.FirstName())
                    .RuleFor(x=> x.LastName, x => x.Name.LastName())
                    .RuleFor(x => x.Phone, x => x.Phone.PhoneNumber())
                    .RuleFor(x => x.Email, x => "admin@gmail.com")
                    .RuleFor(x => x.UserName, x => "admin@gmail.com")
                    .RuleFor(x => x.Image, x => x.Image.LoremPixelUrl());

                AppUser user = faker.Generate();

                var result = userManager.CreateAsync(user, "qwerty").Result;
            }
        }

        private static void GenerateRole(IServiceProvider provider)
        {
            var roleManager = provider.GetRequiredService<RoleManager<AppRole>>();
            if (!roleManager.Roles.Any()) 
            {
                var result = roleManager.CreateAsync(new AppRole {
                    Name = Roles.USER
                }).Result;

                result = roleManager.CreateAsync(new AppRole
                {
                    Name = Roles.ADMIN
                }).Result;
            }
        }
    }
}
