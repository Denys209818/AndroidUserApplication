package com.example.androiduserapplicationphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androiduserapplicationphone.abstractions.AbstractActivity;
import com.example.androiduserapplicationphone.abstractions.stat.RecyclerViewObject;
import com.example.androiduserapplicationphone.adapters.UserViewAdapter;
import com.example.androiduserapplicationphone.network.RetrofitService;
import com.example.androiduserapplicationphone.network.dto.UserDto;
import com.example.androiduserapplicationphone.utils.ProgresDialogUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends AbstractActivity {
    //  Обєкт для відображення колекції карток
    public static RecyclerView rcUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Встановлення шаблону для Activity
        setContentView(R.layout.activity_users);
        //  Ініціалізація RecyclerView
        rcUsers = findViewById(R.id.rcUsers);
        rcUsers.setHasFixedSize(true);
        rcUsers.setLayoutManager(new GridLayoutManager(this, 2,
               LinearLayoutManager.VERTICAL, false ));
        RecyclerViewObject.adapter = new UserViewAdapter();
        rcUsers.setAdapter(RecyclerViewObject.adapter);
        //  Відправка запиту на сервер
        SendRequest();
    }

    private void SendRequest()
    {
        //  Відображення діалогового вікна
        ProgresDialogUtil.showDialog(UsersActivity.this);
        //  Надсилання запитів для отримання користувачів
        RetrofitService.getRetrofitService()
                .getRetrofit().getUsers()
                .enqueue(new Callback<List<UserDto>>() {
                    @Override
                    public void onResponse(Call<List<UserDto>> call, Response<List<UserDto>> response) {
                        if(response.isSuccessful())
                        {
                            //  Ініціалізація колекції користувачів
                            RecyclerViewObject.adapter.users = response.body();
                            RecyclerViewObject.adapter.notifyItemInserted(RecyclerViewObject
                                    .adapter.users.size());
                            //  Закриття діалогового вікна
                            ProgresDialogUtil.hideDialog();
                        }
                        else // Закриття діалогового вікна
                        ProgresDialogUtil.hideDialog();
                    }

                    @Override
                    public void onFailure(Call<List<UserDto>> call, Throwable t) {
                        // Виведення помилки
                        t.printStackTrace();
                        //  Закриття діалогового вікна
                        ProgresDialogUtil.hideDialog();
                    }
                });
    }

}