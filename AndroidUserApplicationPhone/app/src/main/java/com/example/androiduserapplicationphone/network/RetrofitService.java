package com.example.androiduserapplicationphone.network;

import com.example.androiduserapplicationphone.network.interceptors.JwtInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//  Клас, який надає можливість надсилати запити. Прототип Singleton
public class RetrofitService {
    //  Обєкт класу
    private static RetrofitService retrofitService;
    //  Тип для формування інтерфейсу для подальшої відправки запитів
    private Retrofit retrofit;
    //  Базова електронна адреса
    private static String BASE_URL = "http://10.0.2.2:5282";
    //  Повернення базової адреси
    public static String getBaseUrl() {return BASE_URL;}

    private RetrofitService()
    {
        //  Формування OkHttpClient для встановлення часу очікування і встановлення інтерсептора
        //  Для відловлювання запитів
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new JwtInterceptor())
                .build();
        //  Створення обєкту Retrofit.
        this.retrofit = new Retrofit.Builder()
                // Встановлення базової адреси
        .baseUrl(BASE_URL)
                //  Встановлення фабрики для маніпулювання з json форматом
        .addConverterFactory(GsonConverterFactory.create())
                //  Встановлення налаштування для запитів
                .client(httpClient)
                //  Формування обєкту
        .build();
    }
    //  Повернення інтерфейсу для відправки запитів
    public RetrofitApi getRetrofit()
    {
        return retrofit.create(RetrofitApi.class);
    }
    //  Метод, який повертає обєкт даного класу
    public static RetrofitService getRetrofitService() {
        //  Перевірка чи обєкт даного класу не нуль
        if(retrofitService == null)
            //  Ініціалізація обєкту даного класу
            retrofitService = new RetrofitService();
        //  Повернення обєкту даного класу
        return retrofitService;
    }
}
