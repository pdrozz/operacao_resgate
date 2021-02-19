package com.pdrozzsolucoes.gor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.View;

import com.kyanogen.signatureview.SignatureView;
import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.utils.storage.StorageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AssinaturaActivity extends AppCompatActivity {


    private Bitmap mBitmapSignature;
    private SignatureView signatureView;
    private String imageSavedPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinatura);
        signatureView=findViewById(R.id.signature_view);
    }


    public void limparTelaAssinatura(View view) {
        signatureView.clearCanvas();
    }

    public void salvarAssinatura(View view){
        mBitmapSignature = signatureView.getSignatureBitmap();
        imageSavedPath = saveImageAndReturnPath(mBitmapSignature);


        long index = 0;

        while(imageSavedPath == null) {
            index ++;
        }

        Intent i = new Intent();
        i.putExtra("path",imageSavedPath);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public String saveImageAndReturnPath(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = StorageUtil.getAssinaturaPath(this);


        if (!wallpaperDirectory.exists()) wallpaperDirectory.mkdirs();

        try {
            File file=new File(wallpaperDirectory+"/assinatura.jpg");
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());



            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(AssinaturaActivity.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
}
