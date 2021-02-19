package com.pdrozzsolucoes.gor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.model.fichaTratamento.MedicamentoModel;

import java.util.List;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.MedicamentoViewHolder> {


    private List<MedicamentoModel> listMedicamento;
    private Activity activity;

    public MedicamentoAdapter(List<MedicamentoModel> listMedicamento, Activity activity) {
        this.listMedicamento = listMedicamento;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MedicamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_medicamento_item,parent,false);

        return new MedicamentoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentoViewHolder holder, int position) {
        MedicamentoModel model=listMedicamento.get(position);

        holder.textMedicamento.setText(model.getMedicamento());
        holder.textQnt.setText(model.getQnt());
        holder.textDose.setText(model.getDose());
        holder.textVia.setText(model.getVia());
        holder.textDuracao.setText(model.getDuração());
        holder.textPeriodo.setText(model.getPeríodo());


        holder.itemView.setOnClickListener(view -> showAlertDelete(position));

    }

    private void showAlertDelete(final int position){
        String medicamento=listMedicamento.get(position).getMedicamento();
        if (medicamento.length()>20){
            medicamento=medicamento.substring(20)+"...";        }
        AlertDialog.Builder b=new AlertDialog.Builder(activity);
        b.setTitle("Deseja excluir o item da lista?")
                .setMessage("Excluir o medicamento: "+medicamento+" da lista?")
                .setCancelable(false)
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listMedicamento.remove(position);
                        MedicamentoAdapter.this.notifyItemRemoved(position);
                        MedicamentoAdapter.this.notifyDataSetChanged();
                        MedicamentoAdapter.this.notifyItemRangeRemoved(position,position);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    @Override
    public int getItemCount() {
        return listMedicamento.size();
    }

    public class MedicamentoViewHolder extends RecyclerView.ViewHolder{

        TextView textMedicamento,textQnt,textDose,textVia,textDuracao,textPeriodo;
        public MedicamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            textMedicamento=itemView.findViewById(R.id.textMedicamento);
            textQnt=itemView.findViewById(R.id.textQnt);
            textDose=itemView.findViewById(R.id.textDose);
            textVia=itemView.findViewById(R.id.textVia);
            textDuracao=itemView.findViewById(R.id.textDuracao);
            textPeriodo=itemView.findViewById(R.id.textPeriodo);
        }
    }

    public List<MedicamentoModel> getListMedicamento() {
        return listMedicamento;
    }
}
