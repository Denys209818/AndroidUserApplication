package com.example.androiduserapplicationphone.adapters.holders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiduserapplicationphone.EditActivity;
import com.example.androiduserapplicationphone.R;
import com.example.androiduserapplicationphone.abstractions.AbstractActivity;
import com.example.androiduserapplicationphone.abstractions.stat.RecyclerViewObject;
import com.example.androiduserapplicationphone.application.HomeApplication;
import com.example.androiduserapplicationphone.network.RetrofitService;
import com.example.androiduserapplicationphone.network.dto.UserDeleteDto;
import com.example.androiduserapplicationphone.network.dto.UserDto;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//  Холдер, який представляє картку з користувачем
public class UserViewHolder extends RecyclerView.ViewHolder {
    // Обєкт картки
    private View currentView;
    // Обєкт поля для фотографії
    public ImageView userimage;
    // Обєкт текстового поля на картці
    public TextView usrname;
    // Обєкт кнопки редагування на картці
    public Button btnEdit;
    // Обєкт кнопки видалення на картці
    public Button btnDelete;

    public UserViewHolder(@NonNull View itemView) {
        // Викликання батьківського конструктору
        super(itemView);

        // Присвоєння текстовим полям значення з xml-файлу
        currentView = itemView;
        userimage = itemView.findViewById(R.id.userimage);
        usrname = itemView.findViewById(R.id.usrname);
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDelete = itemView.findViewById(R.id.btnDelete);

        // Додавання обробника для події кліку по кнопці видалення
        this.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Створення обєкта, який містить дані про обєкт для видалення
                UserDeleteDto deleteDto = new UserDeleteDto();
                // Встановлення значенння для електронної пошти
                deleteDto.setEmail(usrname.getText().toString());
                // Пошук елемента по вказаному параметру емейлу, щоб видалити на телефоні
                UserDto userDto = RecyclerViewObject.adapter.users.stream()
                        .filter(x -> x.getEmail().equals(usrname.getText().toString()))
                        .collect(Collectors.toList()).stream().findFirst().get();
                // Знаходження позиції елемента у колекції
                int position = RecyclerViewObject.adapter.users.indexOf(userDto);
                // Видалення елемента на телефоні
                RecyclerViewObject.adapter.users.remove(position);
                // Зміна адаптера
                RecyclerViewObject.adapter.notifyItemRemoved(position);
                // Відправка запиту для видалення елемента на сервері
                RetrofitService.getRetrofitService().getRetrofit()
                        .deleteUser(deleteDto)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                // Код при успішному виконанні...
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // Код при не успішному виконанні...
                                t.printStackTrace();
                            }
                        });
            }
        });
        // Додавання обробника для події кліку по кнопці редагування
        this.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Повернення обєкту користувача, емейл користувача якого співпадає
                // з текстовим полем картки
                UserDto userDto = RecyclerViewObject.adapter.users.stream()
                        .filter(x -> x.getEmail().equals(usrname.getText().toString()))
                        .collect(Collectors.toList()).stream().findFirst().get();
                // Ініціалізація інтенту для переходу на сторінки редагування
                Intent intent = new Intent(HomeApplication.getContext(), EditActivity.class);
                // Встановлення прапорція, який дозволяє викликати Actvivty з базового контексту
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Ініціалізація обєкта для передачі данних у новий Activity
                Bundle b = new Bundle();
                //  Запис данних у даний обєкт. Дані Json
                b.putString("user", new Gson().toJson(userDto));
                //  Збереження у інтенті обєкту данних
                intent.putExtras(b);
                // Запуск Activity для редагування користувача
                HomeApplication.getContext().startActivity(intent);
            }
        });
    }

    // Метод, який повертає обєкт картки
    public View getCurrentView() { return this.currentView; }

}
