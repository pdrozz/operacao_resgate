package com.pdrozzsolucoes.gor.utils.storage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;

import java.io.File;

public class StorageUtil {

    private static String TAG="StorageUtil";

    public static void sendPdf(File file,Activity activity){
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("application/pdf");
        Uri uri = FileProvider.getUriForFile(activity, "com.pdrozzsolucoes.fileprovider",file);
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Ocorrência");

        try {
            activity.startActivity(Intent.createChooser(intentShareFile, "Escolha um app"));
        }catch (Exception e){
            AlertDialog.Builder b=new AlertDialog.Builder(activity);
            b.setTitle("Atenção")
                    .setMessage("Aconteceu um erro ao enviar o arquivo, mas ele foi baixado em:"
                            +file.getAbsolutePath()
                    ).setPositiveButton("Entendido",null)
                    .create().show();
        }
    }

    public static File getAnexoPath(Context activity){
        File anexoPath = new File(activity.getCacheDir().getAbsolutePath() + "/anexoOS");

        if (!anexoPath.exists()){
            anexoPath.mkdirs();
        }
        return anexoPath;
    }

    public static File getFichaAnexo(Context activity){
        File anexoPath = new File(activity.getCacheDir().getAbsolutePath() + "/anexoFicha");

        if (!anexoPath.exists()){
            anexoPath.mkdirs();
        }
        return anexoPath;
    }

    public static File getPdfPath(Context activity){
        File pdfPath = activity.getExternalFilesDir("/pdf");

        if (!pdfPath.exists()){
            pdfPath.mkdirs();
        }
        return pdfPath;
    }

    public static File getAssinaturaPath(Context activity){
        File assinaturaPath = new File(activity.getCacheDir().getAbsolutePath() + "/assinatura");

        if (!assinaturaPath.exists()){
            assinaturaPath.mkdirs();
        }

        return assinaturaPath;
    }

    public static File getDownloadPath(Context activity){
        File downloadPath = activity.getExternalFilesDir("/download");

        if (!downloadPath.exists()){
            downloadPath.mkdirs();
        }

        return downloadPath;
    }


    public static void viewPdf(File file,Activity activity){
        Intent intentShareFile = new Intent(Intent.ACTION_VIEW);
        intentShareFile.setType("application/pdf");
        Uri uri = FileProvider.getUriForFile(activity, "com.pdrozzsolucoes.fileprovider",file);
        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Ocorrência");

        try {
            activity.startActivity(Intent.createChooser(intentShareFile, "Escolha um app"));
        }catch (Exception e){
            AlertDialog.Builder b=new AlertDialog.Builder(activity);
            b.setTitle("Atenção")
                    .setMessage("Aconteceu um erro ao abrir automáticamente o arquivo, mas ele foi baixado em:"
                    +file.getAbsolutePath()
                    ).setPositiveButton("Entendido",null)
                    .create().show();
        }

    }

}
