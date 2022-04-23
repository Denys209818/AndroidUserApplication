using AndroidUserApplication.Constants;
using AndroidUserApplication.Data.Identity.Entities;
using Microsoft.AspNetCore.Identity;

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
    }
}
