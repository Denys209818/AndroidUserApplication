namespace AndroidUserApplication.Services
{
    public static class FileLogger
    {
        public static void UseLoggerWithFile(this WebApplication app) 
        {
            using (var scope = app.Services.CreateScope()) 
            {
                var loggerFactory = scope.ServiceProvider.GetRequiredService<ILoggerFactory>();

                string path = Path.Combine(Environment.CurrentDirectory,"Logs");
                if (!Directory.Exists(path)) 
                {
                    Directory.CreateDirectory(path);
                }

                string fileWithLogs = Path.Combine(path, "Log-{Date}.txt");
                loggerFactory.AddFile(fileWithLogs);
            }
        }
    }
}
