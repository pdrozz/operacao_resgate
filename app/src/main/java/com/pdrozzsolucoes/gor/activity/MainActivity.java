package com.pdrozzsolucoes.gor.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.env.env;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        env.showStage(this);
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || false)
                {}
                else{
                    AlertDialog.Builder alert=new AlertDialog.Builder(this);
                    alert.setTitle("Para o app funcionar conceda as permissões")
                            .setMessage(
                                    "O aplicativo gera um PDF e precisa das permissões de armazenamento para poder salva-lo, se já tiver negado reinstale o app ou vá nas configurações do app e conceda as permissões"
                            ).setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getPermissions();
                        }
                    }).setCancelable(false).create().show();
                }
            }else{
            }
        }catch(Exception e){
            Toast.makeText(this, "Ocorreu um erro conceda as permissões do app", Toast.LENGTH_SHORT).show();
        }
        //senha grupogor
    }

    private void getPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},230);
            }
        }
    }

    public void novaOcorrencia(View v){
        startActivity(new Intent(this,NovaOcorrenciaActivity.class));
    }

    public void fichaTratamento(View v){
        startActivity(new Intent(this,FichaTratamentoActivity.class));
    }

    public void Orientacao(View v){
        startActivity(new Intent(this,OrientacoesActivity.class));
    }

    public void buscar(View v){
        startActivity(new Intent(this,ConsultaActivity.class));
    }

}
