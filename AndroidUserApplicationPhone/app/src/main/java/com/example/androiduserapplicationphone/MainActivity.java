package com.example.androiduserapplicationphone;


import android.os.Bundle;
import com.example.androiduserapplicationphone.abstractions.AbstractActivity;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Встановлення шаблону для даного Activity
        setContentView(R.layout.activity_main);
    }
}