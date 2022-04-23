package com.example.androiduserapplicationphone.abstractions;
import com.example.androiduserapplicationphone.LoginActivity;
import com.example.androiduserapplicationphone.MainActivity;
import com.example.androiduserapplicationphone.R;
import com.example.androiduserapplicationphone.UsersActivity;
import com.example.androiduserapplicationphone.application.HomeApplication;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;

//Абстрактний клас для усіх Activity, який містить загальні дані для всіх Activity
public class AbstractActivity extends AppCompatActivity {
    //Метод, який ініціалізує меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Обєкт, який представляє шаблон меню
        MenuInflater inflater = getMenuInflater();
        //Метод присвоює шаблон до обєкта
        inflater.inflate(R.menu.menu_layout, menu);
        //Відображення групи меню, коли користувач не авторизований
        menu.setGroupVisible(R.id.mainGroup, !HomeApplication.getHomeApp().isAuth());
        //Відображення групи меню, коли користувач авторизований
        menu.setGroupVisible(R.id.mainAuthGroup, HomeApplication.getHomeApp().isAuth());
        //Запровадження лінії divider між групами
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }
    //Метод, який оброблює подію обрання пункта меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Встановлення поведінки залежно від елемента
        switch(item.getItemId())
        {
            //Подія переходу на сторінку авторизації
            case R.id.to_login:
                {
                    //Інтент, який вказує Activity, на який буде перехід
                    Intent intent = new Intent(AbstractActivity.this,
                            LoginActivity.class);
                    //Запуск нового Activity
                    startActivity(intent);
                    //Завершення роботи даного Activity
                    finish();
                    return true;
                }
            //Подія переходу на сторінку користувачів
            case R.id.to_users:
                {
                    //Інтент, який вказує Activity, на який буде перехід
                    Intent intent = new Intent(AbstractActivity.this,
                            UsersActivity.class);
                    //Запуск нового Activity
                    startActivity(intent);
                    //Завершення роботи даного Activity
                    finish();
                    return true;

                }
            //Подія переходу на головну сторінку
            case R.id.to_main:
                {
                    //Інтент, який вказує Activity, на який буде перехід
                    Intent intent = new Intent(AbstractActivity.this,
                            MainActivity.class);
                    //Запуск нового Activity
                    startActivity(intent);
                    //Завершення роботи даного Activity
                    finish();
                    return true;

                }
            //Подія виходу з аккаунту
            case R.id.to_logout:
                {
                    //Видалення збереженого токену
                    HomeApplication.getHomeApp().deleteToken();
                    //Інтент, який вказує Activity, на який буде перехід
                    Intent intent = new Intent(AbstractActivity.this,
                            MainActivity.class);
                    //Запуск нового Activity
                    startActivity(intent);
                    //Завершення роботи даного Activity
                    finish();
                    return true;
                }
        }
        return true;
    }
}
