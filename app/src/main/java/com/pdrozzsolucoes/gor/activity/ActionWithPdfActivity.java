package com.pdrozzsolucoes.gor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.helper.ViewMethodsHelper;
import com.pdrozzsolucoes.gor.model.UrlsPdfModel;
import com.pdrozzsolucoes.gor.model.fichaTratamento.FichaTratamentoModel;
import com.pdrozzsolucoes.gor.model.novaOcorrencia.NovaOcorrenciaModel;
import com.pdrozzsolucoes.gor.utils.database.RealtimeConnection;
import com.pdrozzsolucoes.gor.utils.database.RealtimeOcorrencia;
import com.pdrozzsolucoes.gor.utils.storage.StorageConnection;
import com.pdrozzsolucoes.gor.utils.storage.StorageOcorrencia;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionWithPdfActivity extends AppCompatActivity {

    private String pathPdf;
    private StorageOcorrencia storageOcorrencia;
    RealtimeOcorrencia realtimeOcorrencia;
    private NovaOcorrenciaModel model;

    private ProgressBar progress;
    private TextView textTitle;

    public String TYPE_OCORRENCIA="ocorrencia";
    public String TYPE_FICHA_TRATAMENTO="ftratamento";
    public String TYPE_ORIENTACAO="orientacao";

    private UrlsPdfModel urlModel;
    private FichaTratamentoModel fichaModel;

    private String dt,end,n;

    public static final String KEY_MODEL="model";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_with_pdf);

        Bundle data=getIntent().getExtras();


            model=data.getParcelable(KEY_MODEL);
            pathPdf=model.getUrlPdf();
            urlModel=getUrlModel(model);

            storageOcorrencia=new StorageOcorrencia();
            realtimeOcorrencia=new RealtimeOcorrencia(urlModel);


            configWidgets();
            textTitle.setText("Ocorrência nº"+ model.getNumeroOcorrencia()+"\n"+model.getEndereco());
    }

    private UrlsPdfModel getUrlModel(NovaOcorrenciaModel model){
        UrlsPdfModel m=new UrlsPdfModel();
        m.setDt(model.getData());
        m.setEnd(model.getEndereco());
        m.setN(model.getNumeroOcorrencia());
        return m;
    }

    private void configWidgets(){
        progress=findViewById(R.id.progress);
        textTitle=findViewById(R.id.textTitle);
    }

    public void compartilhar(View v){
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(pathPdf);

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/pdf");
            Uri uri = FileProvider.getUriForFile(this, "com.pdrozzsolucoes.fileprovider",fileWithinMyDir);
            intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Ocorrência");

            startActivity(Intent.createChooser(intentShareFile, "Escolha um app"));
        }
    }

    private String getDataToStorage(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }

    public void uploadToStorage(View v){
        final StorageReference refs= StorageConnection.storageReference.child(getDataToStorage()).child(urlModel.getN()+model.getEndereco()+".pdf");
        getAlertDialog("Salvar no banco?","Deseja realizar o upload?")
                .setPositiveButton("Fazer upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ViewMethodsHelper.toggleVisibility(progress);
                        refs.putFile(Uri.fromFile(new File(pathPdf))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ActionWithPdfActivity.this, "Sucesso ao fazer upload!", Toast.LENGTH_SHORT).show();
                                ViewMethodsHelper.toggleVisibility(progress);
                                realtimeOcorrencia.save(urlModel);
                                setDownloadUrl(refs);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ActionWithPdfActivity.this, "Erro ao fazer upload", Toast.LENGTH_SHORT).show();
                                ViewMethodsHelper.toggleVisibility(progress);
                                e.printStackTrace();
                            }
                        });
                    }
                }).create().show();
    }

    private void setDownloadUrl(StorageReference reference ){
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                realtimeOcorrencia.setUrlDownload(urlModel,uri.toString(),realtimeOcorrencia.TYPE_OCORRENCIA);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActionWithPdfActivity.this, "Erro ao fazer upload!", Toast.LENGTH_SHORT).show();
                try{
                    reference.delete();
                }catch (Exception ex){

                }
            }
        });
    }

    public void concluir(View v){
        finish();
    }

    private AlertDialog.Builder getAlertDialog(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message);
        return builder;
    }

}
