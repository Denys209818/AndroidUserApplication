package com.example.androiduserapplicationphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.androiduserapplicationphone.abstractions.AbstractActivity;
import com.example.androiduserapplicationphone.application.HomeApplication;
import com.example.androiduserapplicationphone.network.RetrofitApi;
import com.example.androiduserapplicationphone.network.RetrofitService;
import com.example.androiduserapplicationphone.network.dto.UserLoginDto;
import com.example.androiduserapplicationphone.network.dto.UserLoginedDto;
import com.example.androiduserapplicationphone.network.dto.errors.ErrorMainDto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AbstractActivity {

    //  Обєкти полів
    private TextInputLayout txtLoginLayout;
    private TextInputEditText txtLoginEditText;
    private TextInputLayout txtPasswordLayout;
    private TextInputEditText txtPasswordEditText;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Втсановлення шаблону для Activity
        setContentView(R.layout.activity_login);
        //  Встановлення значень обєктам з xml
        txtLoginEditText = findViewById(R.id.txtLoginInput);
        txtLoginLayout = findViewById(R.id.txtLoginLayout);
        txtPasswordEditText = findViewById(R.id.txtPasswordInput);
        txtPasswordLayout = findViewById(R.id.txtPasswordLayout);
        btnLogin = findViewById(R.id.btnLogin);
    }

    public void onLoginHandler(View view)
    {
        //  Перевірка чи поля валідні
        if(isValidFields())
        {
            //  Формування моделі для логіну користувача
            UserLoginDto loginDto = new UserLoginDto();
            //  Встановлення значень полів
            loginDto.setEmail(this.txtLoginEditText.getText().toString());
            loginDto.setPassword(this.txtPasswordEditText.getText().toString());
            //  Відправка запиту на сервер
            RetrofitService
                    .getRetrofitService()
                    .getRetrofit()
                    .loginUser(loginDto)
                    .enqueue(new Callback<UserLoginedDto>() {
                        @Override
                        public void onResponse(Call<UserLoginedDto> call, Response<UserLoginedDto> response) {
                            if(response.isSuccessful())
                            {
                                //  Формування обєкту, який повертається при успішній авторизації
                                UserLoginedDto logined = response.body();
                                //  Збереження токену
                                HomeApplication.getHomeApp().SaveToken(logined.getToken());
                                //  Формування інтенту для відправки на головну сторінку
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //  Запуск нового Activity
                                startActivity(intent);
                                //  Переривання роботи даного Activity
                                LoginActivity.this.finish();
                            }else
                            {
                                //  Строка для помилок (json)
                                String err = null;
                                try {
                                    // присвоєення json значення
                                    err = response.errorBody().string();
                                    //  Викликання методу, який відображає помилки
                                    showErrorsFromServer(err);
                                } catch (IOException e) {
                                    //  Відображення тектсу помилки
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLoginedDto> call, Throwable t) {
                            //  Відображення тексту помилки
                            t.printStackTrace();
                        }
                    });
        }
    }

    private void showErrorsFromServer(String error) {
        //  Отримання обєкту помилки
        ErrorMainDto err = (new Gson()).fromJson(error, ErrorMainDto.class);
        //  Ініціалізація строки для запису помилок
        String str = "";
        //  Перевірка чи існує масив email
        if(err.errors.email != null)
        {
            //  Прохід масивом і запис усіх помилок у одну строку
        for(String item : err.errors.email)
        {
            str += item;
        }
            //  Присвоєення помилок у текстове поле і шаблон
        this.txtLoginEditText.setError(str);
        this.txtLoginLayout.setError(str);

        }

        //  Перевірка чи існує масив password
        if(err.errors.password != null)
        {
            //  Ініціалізація строки для запису помилок
        str = "";
            //  Прохід масивом і запис усіх помилок у одну строку
            for(String item : err.errors.password)
        {
            str += item;
        }
            //  Присвоєення помилок у текстове поле і шаблон
        this.txtPasswordLayout.setError(str);
        this.txtPasswordEditText.setError(str);
        }
    }

    public Boolean isValidFields()
    {
        //  Встановлення для полів нульові помилки
        txtLoginEditText.setError(null);
        txtPasswordEditText.setError(null);
        txtLoginLayout.setError(null);
        txtPasswordLayout.setError(null);
        //  Перевірка чи поля валідні
        if(!isValidEmailField(txtLoginEditText, txtLoginLayout)
    || !isValidField(txtPasswordEditText, txtPasswordLayout))
        {
            return false;
        }
        return true;
    }

    public Boolean isValidField(TextInputEditText input, TextInputLayout layout)
    {
        //  Отримання тексту помилки
        String text = input.getText().toString();
        //  Перевірка чи текст існує
        if(text == null || text.isEmpty())
        {
            //  Встановлення помилок для текстових полів і шаблонів
            input.setError("Поле не може бути пустим!");
            layout.setError("Поле не може бути пустим!");
            return false;
        }
        return true;
    }

    public Boolean isValidEmailField(TextInputEditText input, TextInputLayout layout)
    {
        //  Ініціалізація тексту
        String text = input.getText().toString();
        //  Перевірка чи існує текст
        if(text == null || text.isEmpty())
        {
            //  Встановлення помилок для полів і шаблонів
            input.setError("Поле не може бути пустим!");
            layout.setError("Поле не може бути пустим!");
            return false;
        }
        //  Перевірка поля на співпадіння з шаблонном електронної пошти
        if(!text
                .matches("^[0-9a-z]+@[a-z]+\\.[a-z]+$"))
        {
            //  Втсановлення помилок для полів і шаблонів
            input.setError("Текст не є електронною поштою!");
            layout.setError("Текст не є електронною поштою!");
            return false;
        }
        return true;
    }
}