package com.example.androiduserapplicationphone.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.androiduserapplicationphone.R;

public class ProgresDialogUtil {
    public ProgresDialogUtil() {}
    //  Статичний обєкт діалогового вікна
    static ProgressDialog progressDialog;
    //  Метод, який відображає модальне вікно
    public static  ProgressDialog showDialog(Context context)
    {
        //  Ініціалізація обєкту. Параметром приймається контекст з якого викликати діалогове вікно
        progressDialog = new ProgressDialog(context);
        //  Відображення діалогового вікна
        progressDialog.show();
        //  Перевірка чи Window не дорівнює null
        if(progressDialog.getWindow() != null)
        {
            //  Встановлення для Window прозорого фону
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        //  Встанолвення шаблону для діалогового вікна
        progressDialog.setContentView(R.layout.progress_dialog);
        //  Метод, який ігнорує прогрес (тобто вказує, що лоадер - це анімація а не прогресБар)
        progressDialog.setIndeterminate(true);
        //  Унеможливлює закриття діалогового вікна нажаттям на скасувати
        progressDialog.setCancelable(false);
        //  Унеможливлює закриття діалогового вікна нажаттям за межі лоадера
        progressDialog.setCanceledOnTouchOutside(false);
        //  Повернення ProgressDialog
        return progressDialog;
    }
    //  Метод, який закриває модальне вікно
    public static void hideDialog()
    {
        //  Перевірка чи обєкт діалогового вікна не дорівнює нулю
        if(progressDialog == null)
        {
            //  Закінчення роботи методу
            return;
        }
        //  Закриття модального вікна
        progressDialog.dismiss();
    }
}
