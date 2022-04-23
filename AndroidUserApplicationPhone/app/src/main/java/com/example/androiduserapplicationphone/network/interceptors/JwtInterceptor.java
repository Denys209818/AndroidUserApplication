package com.example.androiduserapplicationphone.network.interceptors;

import com.example.androiduserapplicationphone.application.HomeApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //  Ініціалізая Request.Builder для внесення конфігурацій
        Request.Builder builder = chain.request().newBuilder();
        //  Перевірка чи існує токен
        if(!HomeApplication.getHomeApp().getToken().isEmpty())
        {
            //  Якщо токен існує, то ініціалізація заголовка для авторизації
            builder.header("Authorization", "Bearer "+
                    HomeApplication.getHomeApp().getToken());
        }
        //  Продовження ланцюжку запиту
        return chain.proceed(builder.build());
    }
}
