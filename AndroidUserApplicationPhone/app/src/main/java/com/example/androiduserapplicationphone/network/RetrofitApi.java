package com.example.androiduserapplicationphone.network;


import com.example.androiduserapplicationphone.network.dto.UserDeleteDto;
import com.example.androiduserapplicationphone.network.dto.UserDto;
import com.example.androiduserapplicationphone.network.dto.UserEditedModel;
import com.example.androiduserapplicationphone.network.dto.UserLoginDto;
import com.example.androiduserapplicationphone.network.dto.UserLoginedDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

//  Інтерфейс Retrofit, який надає методи для відправки на сервер запитів
public interface RetrofitApi {
    //  Запит на отримування користувачів
    @GET("/api/account/users")
    public Call<List<UserDto>> getUsers();
    //  Запит на авторизацію користувача
    @POST("/api/account/login")
    public Call<UserLoginedDto> loginUser(@Body UserLoginDto loginDto);
    //  Метод на видалення користувача
    @POST("/api/account/delete")
    public Call<Void> deleteUser(@Body UserDeleteDto deleteDto);
    //  Метод на редагування користувача
    @POST("/api/account/edit")
    public Call<Void> editUser(@Body UserEditedModel editedModel);
}
