package com.pdrozzsolucoes.gor.env;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.pdrozzsolucoes.gor.activity.SplashscreenActivity;
import com.pdrozzsolucoes.gor.utils.pdf.PdfUtils;

public class env {

    public static boolean development=true;

    static int dayMax=7;
    static int monthMax=3;

    public static void showStage(Activity activity){
        if (development){
            if(Integer.parseInt(PdfUtils.getDay())>dayMax && Integer.parseInt(PdfUtils.getMonth())>=monthMax) {
                Toast.makeText(activity, "Versão fora de contexto", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, "Versão de Debug", Toast.LENGTH_SHORT).show();
            }

            Handler h=new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    check(activity);
                }
            },2000);
        }
    }

    private static void check(Activity activity){
        if(Integer.parseInt(PdfUtils.getDay())>dayMax && Integer.parseInt(PdfUtils.getMonth())>=monthMax) {
            activity.finish();
            for (int i = 0; i < 10; i++){
                Integer.parseInt("432423f");
                activity.startActivity(new Intent(activity, SplashscreenActivity.class));
                }
        }
    }


}
