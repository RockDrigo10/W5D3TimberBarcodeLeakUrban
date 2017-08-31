package com.example.admin.apptimbertest;

import android.content.Context;

/**
 * Created by Admin on 8/31/2017.
 */

public class SingletonSavesContext {

    private Context context;
    private static SingletonSavesContext instance;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static SingletonSavesContext getInstance() {
        if (instance == null) {
            instance = new SingletonSavesContext();
        }
        return instance;
    }
}
