package com.pdrozzsolucoes.gor.utils.database;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pdrozzsolucoes.gor.activity.ActionWithPdfActivity;
import com.pdrozzsolucoes.gor.model.UrlsPdfModel;
import com.pdrozzsolucoes.gor.model.novaOcorrencia.NovaOcorrenciaModel;

public class RealtimeOcorrencia {

    public final String CHILD_DATA="data";
    public final String CHILD_ENDERECO="endereco";
    public final String CHILD_URLS_PDF="urls";
    public final String CHILD_OCORRENCIA="ocorrencia";

    public String TYPE_OCORRENCIA="uOco";
    public String TYPE_TRATAMENTO="uFic";
    public String TYPE_ORIENTACAO="uOri";

    private UrlsPdfModel model;

    public RealtimeOcorrencia(UrlsPdfModel model) {
        this.model = model;
    }

    public void save(UrlsPdfModel model){
        RealtimeConnection.databaseOcorrencia.child(CHILD_OCORRENCIA).child(model.getN().replace("/",""))
                .setValue(model);
    }

    public void setUrlDownload(UrlsPdfModel model,String url,String tipo){
        RealtimeConnection.databaseOcorrencia.child(CHILD_OCORRENCIA).child(model.getN().replace("/",""))
                .child(tipo).setValue(url);
    }

}
