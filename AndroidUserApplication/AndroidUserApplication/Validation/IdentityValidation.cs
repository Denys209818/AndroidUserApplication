using AndroidUserApplication.Models;
using FluentValidation;

namespace AndroidUserApplication.Validation
{
    //Клас для валідації моделі реєстрації користувача
    public class RegsiterValidation : AbstractValidator<RegisterUserModel>
    {
        public RegsiterValidation()
        {
            RuleFor(x => x.Email)
                .EmailAddress().WithMessage("Поле має відповідати вимогам E-mail!")
                .NotEmpty().WithMessage("Поле не може бут пустим!");

            RuleFor(x => x.FirstName)
                .NotEmpty().WithMessage("Поле не може бут пустим!")
                .MinimumLength(2).WithMessage("Має містити не менше 2 символів!");

            RuleFor(x => x.LastName)
                .NotEmpty().WithMessage("Поле не може бут пустим!")
                .MinimumLength(2).WithMessage("Має містити не менше 2 символів!");

            RuleFor(x => x.Phone)
                .NotEmpty().WithMessage("Поле не може бут пустим!")
                .MinimumLength(10).WithMessage("Має містити не менше 10 символів!");

            RuleFor(x => x.Password)
                .NotEmpty().WithMessage("Поле не може бут пустим!")
                .MinimumLength(6).WithMessage("Пароль має містити 6 символів!");

            RuleFor(x => x.ConfirmPassword)
                .NotEmpty().WithMessage("Поле не може бут пустим!")
                .MinimumLength(6).WithMessage("Пароль має містити 6 символів!")
                .Equal(x => x.Password).WithMessage("Поля паролей не співпадають!");
        }
    }
    //Клас для валідації моделі авторизації користувача
    public class LoginValidation : AbstractValidator<LoginUserModel> 
    {
        public LoginValidation()
        {
            RuleFor(x => x.Email)
                .EmailAddress().WithMessage("Поле має відповідати вимогам E-mail!")
                .NotEmpty().WithMessage("Поле не може бут пустим!");


            RuleFor(x => x.Password)
                .NotEmpty().WithMessage("Поле не може бут пустим!")
                .MinimumLength(6).WithMessage("Пароль має містити 6 символів!");
        }
    }
}
