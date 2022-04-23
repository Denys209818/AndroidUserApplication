package com.example.androiduserapplicationphone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.androiduserapplicationphone.abstractions.AbstractActivity;
import com.example.androiduserapplicationphone.application.HomeApplication;
import com.example.androiduserapplicationphone.network.RetrofitService;
import com.example.androiduserapplicationphone.network.dto.UserDto;
import com.example.androiduserapplicationphone.network.dto.UserEditedModel;
import com.example.androiduserapplicationphone.network.dto.errors.ErrorMainDto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//  Activity для редагування користувача
public class EditActivity extends AbstractActivity {
    //  обєкти текстових полів і шаблонів
    private TextInputLayout txtEmailLayout;
    private TextInputEditText txtEmailEditText;
    private TextInputLayout txtFirstNameLayout;
    private TextInputEditText txtFirstNameEditText;
    private TextInputLayout txtSecondNameLayout;
    private TextInputEditText txtSecondNameEditText;
    private TextInputLayout txtPhoneLayout;
    private TextInputEditText txtPhoneEditText;
    private ImageView imgEdit;
    //  Службові змінні для збереження тимчасових данних
    private String _imageBase64 = "";
    private String _oldEmail = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Встановлення шаблону для редагуваня користувачів
        setContentView(R.layout.activity_edit);
        //  Отримання данних з актівіті користувачів
        String json = getIntent().getExtras().getString("user");
        //  Формування обєкту з отриманих данних
        UserDto userDto = new Gson().fromJson(json, UserDto.class);
        //  Присвоєення обєктам значення з xml
        txtEmailEditText = findViewById(R.id.txtEmailInput);
        txtFirstNameEditText = findViewById(R.id.txtFirstNameInput);
        txtSecondNameEditText = findViewById(R.id.txtSecondNameInput);
        txtPhoneEditText = findViewById(R.id.txtPhoneInput);

        txtEmailLayout = findViewById(R.id.txtEmailLayout);
        txtFirstNameLayout = findViewById(R.id.txtFirstNameLayout);
        txtSecondNameLayout = findViewById(R.id.txtSecondNameLayout);
        txtPhoneLayout = findViewById(R.id.txtPhoneLayout);

        imgEdit = findViewById(R.id.imgEdit);
        //  Перевірка чи користувач існує
        if(userDto != null)
        {
            //  Присвоєння полям форми данинх про користувача, які можна змінювати
            String email = userDto.getEmail();
            String firstName = userDto.getFirstName();
            String secondName = userDto.getLastName();
            String phone = userDto.getPhone();
            String image = userDto.getImage();
            this.txtEmailEditText.setText(email);
            this.txtFirstNameEditText.setText(firstName);
            this.txtSecondNameEditText.setText(secondName);
            this.txtPhoneEditText.setText(phone);
            _oldEmail = userDto.getEmail();

            Glide.with(HomeApplication.getContext())
                    .load(RetrofitService.getBaseUrl() + image)
                    .apply(new RequestOptions().override(300, 300))
                    .error(RetrofitService.getBaseUrl() + "/Images/notfound.png")
                    .into(imgEdit);
        }
    }
    //  Метод, який запускає вибірку фотографії
    public void onLoadImage(View view)
    {
        //  Ініціалізація інтенту для вибірки фотографії
        Intent intent = new Intent();
        //  Встановлення типу вибираного файлу
        intent.setType("image/*");
        //  Встановлення дії інтенту
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //  Запуск вікна для вибірки фотографій із кодом запиту 200
        startActivityForResult(Intent.createChooser(intent, "Обрати фотографію"), 200);
    }
    //  Метод, який відправляє дані для редагування на сервер
    public void onChange(View view)
    {
        //  Перевірка чи усі поля валідні
        if(isValidFields())
        {
            //  Ініціалізація моделі для редагування користувача
            UserEditedModel model = new UserEditedModel();
            //  Присвоєння значення полям моделі.
            model.setEmail(this.txtEmailEditText.getText().toString());
            model.setFirstName(this.txtFirstNameEditText.getText().toString());
            model.setLastName(this.txtSecondNameEditText.getText().toString());
            model.setPhone(this.txtPhoneEditText.getText().toString());
            model.setImage(_imageBase64);
            model.setOldEmail(_oldEmail);
            //  Відправка запиту на сервер для зміни данних
            RetrofitService.getRetrofitService().getRetrofit()
                    .editUser(model)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful())
                            {
                                //  Формування інтенту для переходу на сторінку користувачів
                                Intent intent = new
                                        Intent(EditActivity.this, UsersActivity.class);
                                //  Запуск нового Activity
                                startActivity(intent);
                                //  Завершення роботи даного Activity
                                finish();
                            }else
                                {
                                    //  Отримання помилок у форматі JSON та відображення іх
                                    //  на телефоні
                                    String err = null;
                                    try {
                                        //  Отримання помилок у форматі JSON
                                        err = response.errorBody().string();
                                        //  Передача строки у метод для відображення на телефоні
                                        showErrorsFromServer(err);
                                    } catch (IOException e) {
                                        //  Вивід помилки
                                        e.printStackTrace();
                                    }
                                }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            //  Вивід помилки
                            t.printStackTrace();
                        }
                    });
        }
    }
    //  Метод, який валідує усі поля
    public Boolean isValidFields()
    {
        //  Видалення помилок з полів і шаблонів
        this.txtPhoneEditText.setError(null);
        this.txtEmailLayout.setError(null);
        this.txtEmailEditText.setError(null);
        this.txtFirstNameEditText.setError(null);
        this.txtFirstNameLayout.setError(null);
        this.txtSecondNameLayout.setError(null);
        this.txtSecondNameEditText.setError(null);
        this.txtPhoneLayout.setError(null);

        //  Валідація усіх полів
        if(!isValidField(this.txtEmailEditText, this.txtEmailLayout))
            return false;
        if(!isValidField(this.txtFirstNameEditText, this.txtFirstNameLayout))
            return false;
        if(!isValidField(this.txtSecondNameEditText, this.txtSecondNameLayout))
            return false;
        if(!isValidField(this.txtPhoneEditText, this.txtPhoneLayout))
            return false;

        return true;
    }
    //  Метод, який перевіряє поле на валідність
    public Boolean isValidField(TextInputEditText input, TextInputLayout layout)
    {
        //  Повернення тексту з поля
        String text = input.getText().toString();
        //  Перевірка - чи текст існує
        if(text == null || text.isEmpty())
        {
            //  Якщо поле пусте, то встановлюється помилка для полів і шаблонів
            input.setError("Поле не може бути пустим!");
            layout.setError("Поле не може бути пустим!");
            return false;
        }
        return true;
    }

    @Override
    //  Метод, який спарцбовує коли фотографія вибрана
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //  Викликання батьківського методу
        super.onActivityResult(requestCode, resultCode, data);
        //  Перевірка коду результату
        if(resultCode == RESULT_OK)
        {
            //  Перевірка коду запиту
            if(requestCode == 200)
            {
                //  Отримання посилання на фотографію
                Uri img = data.getData();
                //  Присвоєення обєкту ImageView фотографії
                imgEdit.setImageURI(img);

                try
                {
                    //  Формування обєкту Bitmap, який містить у собі обрану фотографію
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), img);
                    //  Ініціалізація потоку байтового масиву
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    //  Запис фотографій у байтовий потік
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    //  Запис коду Base64 у тимчаову змінну
                    _imageBase64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
                }
                catch(Exception ex)
                {
                    //  Виведення помилки
                    ex.printStackTrace();
                }
            }
        }
    }
    //  Метод для відображення помилок із серверу
    private void showErrorsFromServer(String json)
    {
        //  Формування обєкту помилок із json строки
        ErrorMainDto err = (new Gson()).fromJson(json, ErrorMainDto.class);
        //  Запис помилок у одну строку
        String str = "";
        for(String item : err.errors.email)
        {
            str += item;
        }
        //  Присвоєння помилки до полів
        this.txtEmailEditText.setError(str);
        this.txtEmailLayout.setError(str);
    }
}