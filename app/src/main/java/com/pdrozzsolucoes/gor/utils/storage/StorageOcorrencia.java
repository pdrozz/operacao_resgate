package com.pdrozzsolucoes.gor.utils.storage;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pdrozzsolucoes.gor.model.UrlsPdfModel;
import com.pdrozzsolucoes.gor.model.novaOcorrencia.NovaOcorrenciaModel;

import java.io.File;

public class StorageOcorrencia {

    StorageReference ref=StorageConnection.storageReference;

    public UploadTask uploadPdf(File file, UrlsPdfModel model){
        try{
            ref.child(model.getDt()).child(model.getDt()+".pdf").delete();
        }catch (Exception e){

        }
        UploadTask upload=ref.child(model.getDt()).child(model.getN()+".pdf").putFile(Uri.fromFile(file));
        return upload;
    }

}
