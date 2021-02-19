package com.pdrozzsolucoes.gor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.helper.ViewMethodsHelper;
import com.pdrozzsolucoes.gor.model.UrlsPdfModel;
import com.pdrozzsolucoes.gor.utils.database.RealtimeConnection;
import com.pdrozzsolucoes.gor.utils.pdf.PdfUtils;
import com.pdrozzsolucoes.gor.utils.storage.StorageConnection;
import com.pdrozzsolucoes.gor.utils.storage.StorageUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ActionPdfActivity extends AppCompatActivity {


    public final static String TIPO="tipo";
    public final static String TITLE="title";
    public final static String MODEL="MODEL";
    public final static String PATH="PATHPDF";
    public final static String TYPE_FICHA="ficha";
    public final static String TYPE_ORIENTACOES="orientacao";

    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private DatabaseReference databaseReference= RealtimeConnection.databaseOcorrencia;

    private TextView textTitle;
    private ProgressBar progress;
    private UrlsPdfModel model;
    private String path;
    private File pdf;
    private String title;
    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_pdf);

        configWidgets();
        Bundle data=getIntent().getExtras();
        if(data!=null){
            model=data.getParcelable(MODEL);
            path=data.getString(PATH);
            title=data.getString(TITLE);
            tipo=data.getString(TIPO);

            textTitle.setText(title);
            pdf=new File(path);

        }
    }


    private void configWidgets(){
        textTitle=findViewById(R.id.textTitle);
        progress=findViewById(R.id.progress);
    }

    public void compartilhar(View v){
        StorageUtil.sendPdf(pdf,this);
    }

    public void viewPdf(View v){
        StorageUtil.viewPdf(pdf,this);
    }

    private String getDataToStorage(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }

    public void salvarNoBanco(View v){
        final StorageReference ref= StorageConnection.storageReference.child(getDataToStorage()).child(model.getN()+model.getEnd()+".pdf");
        getAlertDialog("Salvar no banco?","Deseja realizar o upload?")
                .setPositiveButton("Fazer upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ViewMethodsHelper.toggleVisibility(progress);
                        ref.putFile(Uri.fromFile(pdf)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ActionPdfActivity.this, "Sucesso ao fazer upload!", Toast.LENGTH_SHORT).show();
                                ViewMethodsHelper.toggleVisibility(progress);
                                if (tipo.equals(TYPE_FICHA)){
                                    setFicha(model);
                                    setDownloadUrl(ref,"ficha");
                                }
                                if (tipo.equals(TYPE_ORIENTACOES)){
                                    setOrientacoes(model);
                                    setDownloadUrl(ref,"orientacao");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ActionPdfActivity.this, "Erro ao fazer upload", Toast.LENGTH_SHORT).show();
                                ViewMethodsHelper.toggleVisibility(progress);
                                e.printStackTrace();
                            }
                        });
                    }
                }).create().show();
    }

    private void setFicha(UrlsPdfModel model){
        model.setDt(PdfUtils.getData());
        databaseReference.child("ficha").child(getDataToStorage().replace("/","")+model.getN().replace("/","")).setValue(model);
    }

    private void setOrientacoes(UrlsPdfModel model){
        databaseReference.child("orientacao").child(model.getN().replace("/","")).setValue(model);
    }

    private void setDownloadUrl(StorageReference ref,String dataRef){
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                databaseReference.child(dataRef).child(getDataToStorage().replace("/","")+model.getN().replace("/","")).child("uOco").setValue(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActionPdfActivity.this, "Erro ao fazer upload!", Toast.LENGTH_SHORT).show();
                try{
                    ref.delete();
                }catch (Exception ex){

                }
            }
        });
    }

    public void concluir(View v){
        finish();
    }

    private AlertDialog.Builder getAlertDialog(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message);
        return builder;
    }
}
