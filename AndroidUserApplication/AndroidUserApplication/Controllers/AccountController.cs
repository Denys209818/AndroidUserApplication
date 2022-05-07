using AndroidUserApplication.Constants;
using AndroidUserApplication.Data.Identity.Entities;
using AndroidUserApplication.Models;
using AndroidUserApplication.Services;
using AutoMapper;
using Bogus;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System.Drawing;
using System.Drawing.Imaging;

namespace AndroidUserApplication.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        //Обєкт для маніпуляцій з користувачами
        private UserManager<AppUser> _userManager { get; set; }
        //Обєкт для маніпуляцій з токеном
        private IJwtBearerService _jwtService { get; set; }
        //Обєкт для маніпулювання з маппером
        private IMapper _mapper { get; set; }
        private ILogger<AccountController> _logger { get; set; }

        public AccountController(UserManager<AppUser> userManager, IJwtBearerService jwtService,
            IMapper mapper, ILogger<AccountController> logger)
        {
            //Ініціалізація властивостей
            _userManager = userManager;
            _jwtService = jwtService;
            _mapper = mapper;
            _logger = logger;
        }

        [HttpPost]
        [Route("login")]
        //Дія для авторизаці користувача
        public async Task<IActionResult> LoginUser([FromBody] LoginUserModel loginModel) 
        {
            return await Task.Run(async () => {

                //_logger.LogError("Error in Account Controller");
                //throw new Exception("Error in login controller");
                //Пошук по електронній адресі користувача
                AppUser user = await _userManager.FindByEmailAsync(loginModel.Email);
                IActionResult res = Ok();
                //Якщо користувача не знайдено повернення помилки
                if (user == null) 
                {
                    ErrorMainModel model = new ErrorMainModel();
                    model.Errors = new ErrorBodyModel();
                    model.Errors.Email = new string[] { "Аккаунта не існує!" };
                    res = BadRequest(model);
                    return res;
                }
                //Перевірка на валідність паролю
                bool isValidPassword = await _userManager.CheckPasswordAsync(user, loginModel.Password);
                //Якщо пароль е праильний повернення помилки
                if (!isValidPassword) 
                {
                    ErrorMainModel model = new ErrorMainModel();
                    model.Errors = new ErrorBodyModel();
                    model.Errors.Password = new string[] { "Пароль введено не коректно!" };
                    res = BadRequest(model);
                    return res;
                }
                res = Ok(new
                {
                    //Генерація токену
                    token = _jwtService.GenerateToken(user)
                });
                //Повернення результату
                return res;
            });
        }

        [HttpPost]
        [Route("register")]
        //Дія для реєстрації користувача
        public async Task<IActionResult> RegisterUser([FromBody] RegisterUserModel registerModel) 
        {
            return await Task.Run(async () => 
            {
                
                IActionResult res = Ok();
                //Пошук користувача по електронній адресі. Якщо знайдено то повертається помилка
                //що дана пошта зареєстрована
                if ((await _userManager.FindByEmailAsync(registerModel.Email)) != null) 
                {
                    res = BadRequest(new
                    {
                        Errors = new
                        {
                            Email = new string[] { "Дана пошта вже зареєстрована!" }
                        }
                    });
                    return res;
                }
                //Створення обєкту надання йому ролі і збереження у БД
                AppUser user = _mapper.Map<AppUser>(registerModel);

                await _userManager.CreateAsync(user, registerModel.Password);
                await _userManager.AddToRoleAsync(user, Roles.USER);
                //Генерація токена
                res = Ok(new { 
                    token = _jwtService.GenerateToken(user)
                });
                return res;
            });
        }
        [HttpGet]
        [Route("users")]
        //Дія для отримання списку користувачів
        public async Task<IActionResult> GetUsers() 
        {
            //Повернення списку користувачів
            return await Task.Run(() => {
                return Ok(_userManager.Users.Select(x => _mapper.Map<UserModel>(x)).ToList());
            });
        }

        [HttpPost]
        [Route("delete")]
        //Дія для видалення користувача
        public async Task<IActionResult> RemoveUser([FromBody] UserDeleteModel deleteModel) 
        {
            return await Task.Run(async () => {
                //Пошук користувача по електронній адресі
                AppUser user = await _userManager.FindByEmailAsync(deleteModel.Email);
                if (user != null) 
                {
                    if (!string.IsNullOrEmpty(user.Image)) 
                    {
                        string path = Path.Combine(Directory.GetCurrentDirectory(), "Images", user.Image);
                        if (System.IO.File.Exists(path)) 
                        {
                            System.IO.File.Delete(path);
                        }
                    }
                    //Видалення користувача
                    await _userManager.DeleteAsync(user);
                }
                return Ok();
            });
        }

        [HttpPost]
        [Route("edit")]
        //Дія для редагування користувача
        public async Task<IActionResult> EditUser([FromBody] UserEditModel model) 
        {
            return await Task.Run(async () => {
                IActionResult res = Ok();
                //Пошук користуача по старій електронній адресі
                AppUser user = await _userManager.FindByEmailAsync(model.OldEmail);
                //Пошук користуача по електронній адресі
                AppUser us = await _userManager.FindByEmailAsync(model.Email);
                //перевірка чи користувач не зареєстрований або чи його старий емейл співпадає з новим
                //щоб не відредагувати існуючий елемент
                if (us == null || user.Email.Equals(us.Email))
                {
                    //Присвоєння значень для полів
                    user.Email = model.Email;
                    user.FirstName = model.FirstName;
                    user.LastName = model.LastName;
                    user.Phone = model.Phone;
                    user.UserName = model.Email;

                    //Перевірка чи поле фотографії не пусте
                    if (!string.IsNullOrEmpty(model.Image))
                    {
                        //Перевірка чи у користувача вже існує фотографія. Якщо існує то видалити
                        if (!string.IsNullOrEmpty(user.Image)) 
                        {
                            //Формування шляху до файлу і видалення файлу
                            string oldPath = Path.Combine(Directory.GetCurrentDirectory(), "Images", user.Image);
                            System.IO.File.Delete(oldPath);
                        }
                        //Формування нового імені для файлу і шляху для фотографії
                        string filename = Path.GetRandomFileName() + ".jpg";
                        string path = Path.Combine(Directory.GetCurrentDirectory(), "Images",
                            filename);
                        //Формування фотографії з строки base64
                        Bitmap bmp = ImageWorker.ConvertToImage(model.Image);
                        //збереження фотографії
                        bmp.Save(path, ImageFormat.Jpeg);
                        //Присвоєння нового імені для фотографії
                        user.Image = filename;
                    }
                    //Обновлення користувача
                    await _userManager.UpdateAsync(user);
                    return res;
                }
                else 
                {
                    //Повернення помилки якщо емейл уже існує в БД
                    ErrorMainModel model = new ErrorMainModel();
                    model.Errors = new ErrorBodyModel();
                    model.Errors.Email = new string[] { "Аккаунт вже зареєстровано!" };
                    return BadRequest(model);
                }

            });
        }

        [HttpGet]
        [Route("randoms/{count}")]
        public async Task<IActionResult> RandomUser([FromRoute]int count) 
        {
            return await Task.Run(() => {
                Faker<UserRandomModel> faker = new Faker<UserRandomModel>("uk")
                .RuleFor(x => x.Email, x => x.Internet.Email())
                .RuleFor(x => x.Phone, x => x.Phone.PhoneNumber())
                .RuleFor(x => x.FirstName, x => x.Name.FirstName())
                .RuleFor(x => x.LastName, x => x.Name.LastName());

                List<UserRandomModel> models = new List<UserRandomModel>();

                for (int i = 0; i < count; i++) 
                {
                    UserRandomModel user = faker.Generate();
                    user.Key = i.ToString();
                    models.Add(user);
                }

                return Ok(models);
            });
        }
    }
}
