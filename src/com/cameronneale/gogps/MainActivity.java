package com.cameronneale.gogps;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {
boolean settings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        Object lg = (Object) findViewById(R.id.titleLogo);
        ObjectAnimator anim = ObjectAnimator.ofFloat(lg, "alpha", 0f, 1f);  
        anim.setDuration(1500);
        anim.start();
        new Handler().postDelayed(new Runnable() {
        	public void run() {
        	startActivity(new Intent(MainActivity.this, MapLauncher.class));
        	finish();}
        }, 3000);
    }
    
   


    
}