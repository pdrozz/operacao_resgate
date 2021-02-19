package com.pdrozzsolucoes.gor.helper;

import android.view.View;

public class ViewMethodsHelper {

    public static void toggleVisibility(View v,int typeInvisible){
        if (v.getVisibility()==View.VISIBLE){
            v.setVisibility(typeInvisible);
        }
        else{
            v.setVisibility(View.VISIBLE);
        }
    }

    public static void toggleVisibility(View v){
        if (v.getVisibility()==View.VISIBLE){
            v.setVisibility(View.GONE);
        }
        else{
            v.setVisibility(View.VISIBLE);
        }
    }

}
