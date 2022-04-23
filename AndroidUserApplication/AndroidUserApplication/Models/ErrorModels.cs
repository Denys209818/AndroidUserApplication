namespace AndroidUserApplication.Models
{
    public class ErrorMainModel
    {
        public ErrorBodyModel Errors { get; set; }
    }
    public class ErrorBodyModel 
    {
        public string[] Email { get; set; }
        public string[] Password { get; set; }
    }
}
