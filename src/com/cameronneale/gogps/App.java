package com.cameronneale.gogps;

import android.app.Application;
import android.content.Context;

public class App extends Application{

    private static Context con;

    @Override
    public void onCreate() {
        super.onCreate();
        con = this;
    }

    public static Context getContext(){
        return con;
    }
}
