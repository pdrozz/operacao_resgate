package com.pdrozzsolucoes;

import android.app.Application;
import android.util.Log;

import com.pdrozzsolucoes.gor.utils.storage.StorageUtil;

import java.io.File;

public class App extends Application {

    private String TAG="GOR_APP";

    @Override
    public void onCreate() {
        try {
            for(File f : StorageUtil.getAssinaturaPath(this).listFiles()){
                Log.i(TAG, "onCreate: "+f.getName());
                f.delete();
            }
            for(File f : StorageUtil.getFichaAnexo(this).listFiles()){
                Log.i(TAG, "onCreate: "+f.getName());
                f.delete();
            }

        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
        }
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
