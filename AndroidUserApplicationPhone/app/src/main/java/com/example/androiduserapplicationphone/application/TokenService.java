package com.example.androiduserapplicationphone.application;

//  Інтерфейс, який надає методи для маніпуляції з токеном
public interface TokenService {
    public void SaveToken(String token);
    public String getToken();
    public void deleteToken();
    public boolean isAuth();
}
