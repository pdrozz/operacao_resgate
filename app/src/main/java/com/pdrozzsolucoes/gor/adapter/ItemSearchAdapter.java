package com.pdrozzsolucoes.gor.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.model.UrlsPdfModel;
import com.pdrozzsolucoes.gor.utils.storage.StorageUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchAdapter.ItemSearchViewHolderAdapter> {

    private List<UrlsPdfModel> listItems;
    private Activity activity;

    public ItemSearchAdapter(List<UrlsPdfModel> listItems, Activity activity) {
        this.listItems = listItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemSearchViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_item,parent,false);

        return new ItemSearchViewHolderAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSearchViewHolderAdapter holder, int position) {
        UrlsPdfModel model=listItems.get(position);

        initLinear(model,holder);
        if (model.getEnd()==null || model.getEnd().equals("")){
            holder.textEnde.setVisibility(View.GONE);
            holder.textCep.setVisibility(View.GONE);
        }else{
            holder.textCep.setText(model.getEnd());
        }
        holder.textNumero.setText(model.getN());
        holder.textData.setText(model.getDt());


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private void initLinear(UrlsPdfModel model,ItemSearchViewHolderAdapter holder){

        if (model.getuFic()!=null){
            if (model.getuFic().length()>5){
                holder.linearTratamento.setVisibility(View.VISIBLE);
                holder.linearTratamento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       Toast.makeText(activity, "Baixando!", Toast.LENGTH_SHORT).show();
                        File folder = StorageUtil.getDownloadPath(activity);
                        if (!folder.exists())
                            folder.mkdirs();

                        File file=new File(folder,model.getN().replace("/","")+"-FichaTratamento.pdf");
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl(model.getuFic());
                        ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(activity, "Baixado com sucesso!", Toast.LENGTH_SHORT).show();
                                StorageUtil.sendPdf(file,activity);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Erro ao baixar", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });
            }
        }
        if (model.getuOco()!=null){
            if (model.getuOco().length()>5){
                holder.linearOcorrencia.setVisibility(View.VISIBLE);
                holder.linearOcorrencia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(activity, "Baixando!", Toast.LENGTH_SHORT).show();
                        File folder = StorageUtil.getDownloadPath(activity);
                        if (!folder.exists())
                            folder.mkdirs();

                        File file=new File(folder+"/"+model.getN().replace("/","-")+"-Ocorrencia.pdf");
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl(model.getuOco());
                        ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(activity, "Baixado com sucesso!", Toast.LENGTH_SHORT).show();
                                StorageUtil.sendPdf(file,activity);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Erro ao baixar", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        }

        if (model.getU()!=null){
            if (model.getU().length()>5){
                holder.linearOrientacao.setVisibility(View.VISIBLE);
                holder.linearOrientacao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(activity, "Baixando!", Toast.LENGTH_SHORT).show();
                        File folder = StorageUtil.getDownloadPath(activity);
                        if (!folder.exists())
                            folder.mkdirs();

                        File file=new File(folder+"/"+model.getN().replace("/","")+".pdf");
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        StorageReference ref= FirebaseStorage.getInstance().getReferenceFromUrl(model.getuOri());
                        ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(activity, "Baixado com sucesso!", Toast.LENGTH_SHORT).show();
                                StorageUtil.sendPdf(file,activity);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Erro ao baixar", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        }
    }


    public class ItemSearchViewHolderAdapter extends RecyclerView.ViewHolder{
        ImageView imageOcorrencia,imageFichaTratamento,imageOrientacoes;
        LinearLayout linearOcorrencia,linearOrientacao,linearTratamento;
        TextView textNumero,textCep,textData,textEnde;
        public ItemSearchViewHolderAdapter(@NonNull View itemView) {
            super(itemView);
            imageOcorrencia=itemView.findViewById(R.id.imageOcorrencia);
            imageFichaTratamento=itemView.findViewById(R.id.imageFichaTratamento);
            imageOrientacoes=itemView.findViewById(R.id.imageOrientacoes);
            //---
            linearOcorrencia=itemView.findViewById(R.id.linearOcorrencia);
            linearOrientacao=itemView.findViewById(R.id.linearOrientacao);
            linearTratamento=itemView.findViewById(R.id.linearTratamento);
            textNumero=itemView.findViewById(R.id.textNumero);
            textCep=itemView.findViewById(R.id.textCep);
            textData=itemView.findViewById(R.id.textData);
            textEnde=itemView.findViewById(R.id.textEnde);
        }
    }



}
