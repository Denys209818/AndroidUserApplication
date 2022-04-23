package com.example.androiduserapplicationphone.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.androiduserapplicationphone.network.dto.UserDto;

import java.util.List;
//  Клас, який представляє контекст додатку
public class HomeApplication extends Application implements TokenService {

    //  Статична колекція користувачів
    public static List<UserDto> users;
    //  Контекст, з якого можна надсилати запити від додатку
    private static Context context;
    //  Статичний обєкт самого класу. Прототип паттерну Singleton
    private static HomeApplication homeApplication;
    //  Метод, який повертає HomeApplication
    public static HomeApplication getHomeApp()
    {
        return homeApplication;
    }
    // Метод, який повертає Контекст
    public static Context getContext()
    {
        return context;
    }
    // Метод, який встановлює контекст
    public void setAppContext(Context _context)
    {
        context = _context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //  Ініціалізація HomeApplication
        homeApplication = this;
        //  Встановлення значення контексту
        setAppContext(this.getApplicationContext());
        //  Дозволяння векторних зображень
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void SaveToken(String token) {
        //  Ініціаліація обєкту SharedPreferences і повернення комірки jwtToken
        SharedPreferences prefs = getSharedPreferences("jwtToken", MODE_PRIVATE);
        // Ініціалізація Editor і присвоєння значення
        SharedPreferences.Editor editor = prefs.edit();
        // Запис у сховище токену
        editor.putString("token", token);
        // Підтвердження змін у сховищі
        editor.commit();
    }

    @Override
    public String getToken() {
        //  Ініціаліація обєкту SharedPreferences і повернення комірки jwtToken
        SharedPreferences preferences = getSharedPreferences("jwtToken", MODE_PRIVATE);
        //  Повернення токену і присвоєння значення у строку
        String token = preferences.getString("token", "");
        //  Повернення токену
        return token;
    }

    @Override
    public boolean isAuth() {
        //  Перевірка наявності токена
        if(getToken().equals(""))
            return false;
        return true;
    }

    @Override
    public void deleteToken() {
        //  Ініціаліація обєкту SharedPreferences і повернення комірки jwtToken
        SharedPreferences preferences =getSharedPreferences("jwtToken", MODE_PRIVATE);
        // Ініціалізація Editor і присвоєння значення
        SharedPreferences.Editor editor = preferences.edit();
        try
        {
            //  Видалення токену
            editor.remove("token");
            //  Фіксація данних
            editor.commit();
            //  Фіксація данних
            editor.apply();
        }
        catch(Exception ex)
        {
            //  Виведення тексту помилки
            ex.printStackTrace();
        }
    }


}
