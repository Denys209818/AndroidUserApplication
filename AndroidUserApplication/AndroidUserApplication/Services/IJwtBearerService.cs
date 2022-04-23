using AndroidUserApplication.Data.Identity.Entities;
using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace AndroidUserApplication.Services
{
    //Інтерфейс який надає метод для генерації токену
    public interface IJwtBearerService
    {
        string GenerateToken(AppUser user);
    }

    public class JwtBearerService : IJwtBearerService
    {
        //Менеджер для маніпуляції користувачами
        private UserManager<AppUser> _userManager { get; set; }
        //Сервіс конфігурацій
        private IConfiguration _configuration { get; set; }
        public JwtBearerService(UserManager<AppUser> userManager,
            IConfiguration configuration)
        {
            //Ініціалізація полів
            _userManager = userManager;
            _configuration = configuration;
        }
        public string GenerateToken(AppUser user)
        {
            //Ініціалізація клеймів для токена
            List<Claim> claims = new List<Claim>();
            claims.Add(new Claim("fistname", user.FirstName));
            claims.Add(new Claim("lastname", user.LastName));
            claims.Add(new Claim("email", user.Email));
            claims.Add(new Claim("phone", user.Phone));

            //Формування кприватного ключа
            var symmetricKey = new SymmetricSecurityKey(
                Encoding.UTF8.GetBytes(_configuration.GetValue<String>("private_key")));
            //Ініціалізація класу для шифрування ключа
            SigningCredentials signingCredentials = new SigningCredentials(symmetricKey, 
                SecurityAlgorithms.HmacSha256);
            //Створення обєкту токена
            var token = new JwtSecurityToken(claims: claims, signingCredentials: signingCredentials,
                expires: DateTime.Now.AddDays(100));
            //Генерація токена
            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
