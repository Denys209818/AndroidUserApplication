package com.example.androiduserapplicationphone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.androiduserapplicationphone.R;
import com.example.androiduserapplicationphone.adapters.holders.UserViewHolder;
import com.example.androiduserapplicationphone.application.HomeApplication;
import com.example.androiduserapplicationphone.network.RetrofitService;
import com.example.androiduserapplicationphone.network.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserViewAdapter extends RecyclerView.Adapter<UserViewHolder> {
    //  Колекція користувачів
    public List<UserDto> users;
    //  Конструктор, який ініціалізує колекцію користувачів
    public UserViewAdapter()
    {
        this.users = new ArrayList<>();
    }
    @NonNull
    @Override
    //  Метод, який формує холдер з привязкою до картки
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  Формування картки. Метод from приймає контекст, з якого вибирається ресурс.
        View item = LayoutInflater.from(HomeApplication.getContext())
                //  Метод inflate формує View. Приймає ідентифікатор шаблона картки
                //  Другий параметр батьківський елемент, Третій параметр чи додавати у корінь
                //  xml, а не у параметри RecyclerView
                .inflate(R.layout.card_layout, parent, false);
        //  Повернення створеного холдеру
        return new UserViewHolder(item);
    }

    @Override
    //  Метод, який звязує обєкт користувача з холдером
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Перевірка чи масив не нульовий і позиція менша за розмір
        if(users != null && position < users.size())
        {
            // Отримання елемента за позицією
            UserDto userDto = users.get(position);
            //  Присвоєння тексту для текстового поля картки
            holder.usrname.setText(userDto.getEmail());
            //  Присвоєення фотографії для поля фотографій
            Glide.with(HomeApplication.getContext())
                    //  Завантаження інтернет-ресурсу
                    .load(RetrofitService.getBaseUrl() + userDto.getImage())
                    //  Виконання запиту і встановлення розміру. Повертає білдер запиту
                    .apply(new RequestOptions().override(300, 300))
                    //  Встановлення місця для завантаження інтрнет-ресурса
                    .into(holder.userimage);
        }
    }


    @Override
    //  Метод, який повертає розмір масиву з користувачами
    public int getItemCount() {
        return users.size();
    }
}
