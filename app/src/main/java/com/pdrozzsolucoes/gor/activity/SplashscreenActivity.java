package com.pdrozzsolucoes.gor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.env.env;
import com.pdrozzsolucoes.gor.utils.auth.AuthUtils;

public class SplashscreenActivity extends AppCompatActivity {

    private Handler h;
    private boolean isLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        isLogged=AuthUtils.isLogged();
        splash();

        env.showStage(this);
    }

    private void splash(){
        h=new Handler();
        if(isLogged){
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToApp();
                    finish();
                }
            },2500);
        }
        else{
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLogin();
                    finish();
                }
            },2500);
        }
    }

    private void goToApp(){
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

    private void goToLogin(){
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
    }


}
