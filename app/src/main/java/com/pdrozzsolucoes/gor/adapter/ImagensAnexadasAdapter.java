package com.pdrozzsolucoes.gor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.model.ImagemAnexadaModel;

import java.io.File;
import java.util.List;

public class ImagensAnexadasAdapter extends RecyclerView.Adapter<ImagensAnexadasAdapter.ImagensAnexadasViewHolder> {

    private List<ImagemAnexadaModel> listImagens;
    private Activity activity;

    public ImagensAnexadasAdapter(List<ImagemAnexadaModel> listImagens, Activity activity) {
        this.listImagens = listImagens;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ImagensAnexadasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_grid,parent,false);
        return new ImagensAnexadasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagensAnexadasViewHolder holder, final int position) {
        File file=listImagens.get(position).getFile();

        Glide.with(activity).load(file).into(holder.image);
        String TAG="ANEXADAS";
        Log.i(TAG, "onBindViewHolder: "+file.getAbsolutePath());

        final View v=holder.view;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(v.getVisibility()==View.VISIBLE){
                    v.setVisibility(View.GONE);
                    listImagens.get(position).setSelected(false);
                }else{
                    v.setVisibility(View.VISIBLE);
                    listImagens.get(position).setSelected(true);
                }
            }
        });
    }

    public void showAlertDeleteSelecionado(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Excluir itens selecionados?")
                .setMessage("Realmente deseja excluir os itens que foram selecionados? Essa operação não pode ser desfeita")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSelecionado();
                        activity.finish();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        }).setCancelable(false)
                .create().show();
    }

    private void deleteSelecionado(){
        for(ImagemAnexadaModel model:listImagens){
            if (model.isSelected()){
                this.notifyItemRemoved(listImagens.indexOf(model));
                this.notifyDataSetChanged();
                model.delete();
            }
        }
    }

    @Override
    public int getItemCount() {
        return listImagens.size();
    }

    public class ImagensAnexadasViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        View view;
        public ImagensAnexadasViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            view=itemView.findViewById(R.id.view);

        }
    }

}
