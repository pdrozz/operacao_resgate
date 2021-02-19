package com.pdrozzsolucoes.gor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.adapter.ImagensAnexadasAdapter;
import com.pdrozzsolucoes.gor.model.ImagemAnexadaModel;
import com.pdrozzsolucoes.gor.utils.storage.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagensAnexadasActivity extends AppCompatActivity {

    private List<ImagemAnexadaModel> listFile=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImagensAnexadasAdapter adapter;
    private String TAG="ANEXADAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagens_anexadas);

        getList();
        adapter=new ImagensAnexadasAdapter(listFile,this);

        recyclerView=findViewById(R.id.recycler);
        layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void getList(){
        listFile=new ArrayList<>();
        try{
            //ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = StorageUtil.getAnexoPath(this);
        if(directory.listFiles().length>0) {
            for (File f :directory.listFiles()) {
                ImagemAnexadaModel model = new ImagemAnexadaModel();
                model.setFile(f);
                listFile.add(model);
            }
        }
        }catch (Exception e){

        }
    }

    public void deleteSelecionadas(View v){
        adapter.showAlertDeleteSelecionado();
        getList();
        adapter=new ImagensAnexadasAdapter(listFile,this);
        recyclerView.setAdapter(adapter);
    }
}
