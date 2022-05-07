namespace AndroidUserApplication.Models
{
    public class GlobalErrorModel
    {
        public string Message { get; set; }
        public string Path { get; set; }
        public GlobalErrorModel(string message, string path)
        {
            this.Message = message;
            this.Path = path;
        }
    }
}
