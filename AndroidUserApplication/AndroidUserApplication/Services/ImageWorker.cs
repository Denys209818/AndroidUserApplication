using System.Drawing;

namespace AndroidUserApplication.Services
{
    public class ImageWorker
    {
        //Метод, який конвертує строку base64 у Bitmap
        public static Bitmap ConvertToImage(string base64)
        {
            try
            {
                //Конвертація у байтовий масив
                byte[] img = Convert.FromBase64String(base64);
                //Використання потоку памяті для збереження байтового масиву фотографії
                using (MemoryStream memory = new MemoryStream(img))
                {
                    //Формування фотографії і повернення її у форматі Bitmap
                    memory.Position = 0;
                    Image image = Image.FromStream(memory);
                    memory.Close();
                    img = null;
                    return new Bitmap(image);
                }
            }
            catch
            {
                return null;
            }
        }
    }
}
