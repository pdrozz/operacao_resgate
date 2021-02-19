package com.pdrozzsolucoes.gor.utils;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.Constants;

public class AnalyticsUtil {

    public static String generateOcorrencia="OcorrenciaGerada";
    public static String generateFichaTratamento="FichaTratamentoGerada";
    public static String generateOrientacoes="OrientacoesGerada";
    public static String consulta="consultaRealizada";


    public static void savePdfGerado(Activity activity,String tipoPdf){
        FirebaseAnalytics analytics=FirebaseAnalytics.getInstance(activity);
        Bundle b=new Bundle();
        b.putString("Classe",tipoPdf);
        analytics.logEvent("PdfGerado",b);
    }

}
