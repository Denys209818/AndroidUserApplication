using AndroidUserApplication.Models;
using Newtonsoft.Json;
using System.Net;

namespace AndroidUserApplication.Middleware
{
    public class GlobalExceptionHandler 
    {
        private RequestDelegate _handler;

        public GlobalExceptionHandler(RequestDelegate requestDelegate)
        {
            _handler = requestDelegate;
        }

        public async Task Invoke(HttpContext http) 
        {
            try
            {
                await _handler(http);
            }
            catch (Exception ex) 
            {
                await CatchingError(http, ex);
            }
        }

        private Task CatchingError(HttpContext http, Exception ex) 
        {
            int code = (int)HttpStatusCode.InternalServerError;

            string result = JsonConvert.SerializeObject(new
            {
                errors = new GlobalErrorModel(ex.Message, ex.StackTrace)
            });

            var response = http.Response;
            response.StatusCode = code;
            response.ContentType = "application/json";

            return response.WriteAsync(result);
        }
    }
    public static class ExceptionHandlerMiddleware
    {
        public static IApplicationBuilder UseGlobalExceptionHandler(this IApplicationBuilder builder) 
        {
            return builder.UseMiddleware<GlobalExceptionHandler>();
        }
    }
}
