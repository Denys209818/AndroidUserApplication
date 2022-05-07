namespace AndroidUserApplication.Models
{
    public class RegisterUserModel
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Image { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string ConfirmPassword { get; set; }

    }

    public class LoginUserModel
    {
        public string Email { get; set; }
        public string Password { get; set; }
    }

    public class UserModel
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Image { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }
    }

    public class UserRandomModel
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Key { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }
    }

    public class UserDeleteModel
    {
        public string Email { get; set; }
    }

    public class UserEditModel
    {
        public string Email { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Phone { get; set; }
        public string Image { get; set; }
        public string OldEmail { get; set; }
    }
}
