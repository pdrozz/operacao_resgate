package com.pdrozzsolucoes.gor.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.helper.ViewMethodsHelper;
import com.pdrozzsolucoes.gor.model.UrlsPdfModel;
import com.pdrozzsolucoes.gor.model.fichaTratamento.FichaTratamentoModel;
import com.pdrozzsolucoes.gor.model.orientacoes.OrientacoesModel;
import com.pdrozzsolucoes.gor.model.orientacoes.OrientacoesViewModel;
import com.pdrozzsolucoes.gor.utils.MaskText;
import com.pdrozzsolucoes.gor.utils.pdf.PdfUtils;
import com.pdrozzsolucoes.gor.utils.storage.StorageUtil;

import java.io.File;

public class OrientacoesActivity extends AppCompatActivity {

    private EditText obs,editData,editHora,editEndereco,editNrOcorrencia;

    //proprietario
    private EditText editNascimento,editCpf,editNomeProprietario;
    private CheckBox checkAssinaturaProprietario;
    private LinearLayout linearAssinaturaProprietario;

    private CheckBox checkNutricao1,checkNutricao3,checkNutricao2,checkNutricao4,checkNutricao5,checkNutricao6;
    private CheckBox checkSanidade1,checkSanidade3,checkSanidade2,checkSanidade4;
    private CheckBox checkConforto1,checkConforto3,checkConforto2,checkConforto4,checkConforto5,checkConforto6;
    private CheckBox checkComportamento1,checkComportamento3,checkComportamento2,checkComportamento4,checkComportamento5;

    private OrientacoesModel model;

    private OrientacoesViewModel viewModel;

    private String pathAssinaturaResponsavel, pathAssinaturaOperacao;

    private EditText input;

    private final int ASSINATURA_RESPONSAVEL=200;
    private final int ASSINATURA_OPERACAO=140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientacoes);

        viewModel=new ViewModelProvider(this).get(OrientacoesViewModel.class);
        configWidgets();
        setModelInView();
        checkListener();
        editHora.addTextChangedListener(MaskText.mask(editHora,MaskText.FORMAT_HOUR));
        editData.addTextChangedListener(MaskText.mask(editData,MaskText.FORMAT_DATE));
        editNrOcorrencia.addTextChangedListener(MaskText.mask(editNrOcorrencia,MaskText.FORMAT_OCORRENCIA));
        editNascimento.addTextChangedListener(MaskText.mask(editNascimento,MaskText.FORMAT_DATE));
        editCpf.addTextChangedListener(MaskText.mask(editCpf,MaskText.FORMAT_CPF));
    }

    private void configWidgets(){
        obs=findViewById(R.id.editObs);
        editData=findViewById(R.id.editData);
        editHora=findViewById(R.id.editHora);
        editEndereco=findViewById(R.id.editEndereco);
        editNrOcorrencia=findViewById(R.id.editNrOcorrencia);

        editNascimento=findViewById(R.id.editNascimento);
        editCpf=findViewById(R.id.editCpf);
        editNomeProprietario=findViewById(R.id.editNomeProprietario);
        checkAssinaturaProprietario=findViewById(R.id.checkAssinaturaProprietario);
        linearAssinaturaProprietario=findViewById(R.id.linearAssinaturaProprietario);

        checkNutricao1=findViewById(R.id.checkNutricao1);
        checkNutricao2=findViewById(R.id.checkNutricao2);
        checkNutricao3=findViewById(R.id.checkNutricao3);
        checkNutricao4=findViewById(R.id.checkNutricao4);
        checkNutricao5=findViewById(R.id.checkNutricao5);
        checkNutricao6=findViewById(R.id.checkNutricao6);

        checkSanidade1=findViewById(R.id.checkSanidade1);
        checkSanidade2=findViewById(R.id.checkSanidade2);
        checkSanidade3=findViewById(R.id.checkSanidade3);
        checkSanidade4=findViewById(R.id.checkSanidade4);

        checkConforto1=findViewById(R.id.checkConforto1);
        checkConforto2=findViewById(R.id.checkConforto2);
        checkConforto3=findViewById(R.id.checkConforto3);
        checkConforto4=findViewById(R.id.checkConforto4);
        checkConforto5=findViewById(R.id.checkConforto5);
        checkConforto6=findViewById(R.id.checkConforto6);

        checkComportamento1=findViewById(R.id.checkComportamento1);
        checkComportamento2=findViewById(R.id.checkComportamento2);
        checkComportamento3=findViewById(R.id.checkComportamento3);
        checkComportamento4=findViewById(R.id.checkComportamento4);
        checkComportamento5=findViewById(R.id.checkComportamento5);
    }

    private void checkListener(){
        checkAssinaturaProprietario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ViewMethodsHelper.toggleVisibility(linearAssinaturaProprietario);
            }
        });
    }

    private boolean getCheckBoxIsChecked(int id){
        CheckBox c=findViewById(id);
        return c.isChecked();
    }

    private void setModelInView(){
        model=viewModel.getModel();
        checkSanidade1.setChecked(model.isSanidade1());
        checkSanidade2.setChecked(model.isSanidade2());
        checkSanidade3.setChecked(model.isSanidade3());
        checkSanidade4.setChecked(model.isSanidade4());

        checkConforto1.setChecked(model.isConforto1());
        checkConforto2.setChecked(model.isConforto2());
        checkConforto3.setChecked(model.isConforto3());
        checkConforto4.setChecked(model.isConforto4());
        checkConforto5.setChecked(model.isConforto5());
        checkConforto6.setChecked(model.isConforto6());

        checkComportamento1.setChecked(model.isComportamento1());
        checkComportamento2.setChecked(model.isComportamento2());
        checkComportamento3.setChecked(model.isComportamento3());
        checkComportamento4.setChecked(model.isComportamento4());
        checkComportamento5.setChecked(model.isComportamento5());


        checkNutricao1.setChecked(model.isNutricao1());
        checkNutricao2.setChecked(model.isNutricao2());
        checkNutricao3.setChecked(model.isNutricao3());
        checkNutricao4.setChecked(model.isNutricao4());
        checkNutricao5.setChecked(model.isNutricao5());
        checkNutricao6.setChecked(model.isNutricao6());

        checkAssinaturaProprietario.setChecked(model.isProprietarioAssinou());

        editCpf.setText(model.getCpf());
        editNascimento.setText(model.getDataNascimento());
        editNomeProprietario.setText(model.getNomeCompletoProprietario());
        obs.setText(model.getObs());
        editEndereco.setText(model.getEndereco());
        editData.setText(model.getData());
        editHora.setText(model.getHora());
    }

    private void setTextsModel(){
        viewModel.getModel().setObs(obs.getText().toString());
        viewModel.getModel().setEndereco(editEndereco.getText().toString());
        viewModel.getModel().setData(editData.getText().toString());
        viewModel.getModel().setHora(editHora.getText().toString());
        viewModel.getModel().setTipoAnimal(getTipoAnimal());
        viewModel.getModel().setNumeroOcorrencia(editNrOcorrencia.getText().toString());

        viewModel.getModel().setNomeCompletoProprietario(editNomeProprietario.getText().toString());
        viewModel.getModel().setCpf(editCpf.getText().toString());
        viewModel.getModel().setDataNascimento(editNascimento.getText().toString());
        viewModel.getModel().setProprietarioAssinou(getCheckBoxIsChecked(R.id.checkAssinaturaProprietario));
        viewModel.getModel().setAssinaturaOperacao(pathAssinaturaOperacao);
        if (!getCheckBoxIsChecked(R.id.checkAssinaturaProprietario)){
            viewModel.getModel().setAssinaturaResponsavel(pathAssinaturaResponsavel);
        }
    }

    private void setCheckConfortoModel(){
        viewModel.getModel().setConforto1(checkConforto1.isChecked());
        viewModel.getModel().setConforto2(checkConforto2.isChecked());
        viewModel.getModel().setConforto3(checkConforto3.isChecked());
        viewModel.getModel().setConforto4(checkConforto4.isChecked());
        viewModel.getModel().setConforto5(checkConforto5.isChecked());
        viewModel.getModel().setConforto6(checkConforto6.isChecked());
    }

    private void setCheckSanidadeModel(){
        viewModel.getModel().setSanidade1(checkSanidade1.isChecked());
        viewModel.getModel().setSanidade2(checkSanidade2.isChecked());
        viewModel.getModel().setSanidade3(checkSanidade3.isChecked());
        viewModel.getModel().setSanidade4(checkSanidade4.isChecked());
    }

    private void setCheckNutricaoModel(){
        viewModel.getModel().setNutricao1(checkNutricao1.isChecked());
        viewModel.getModel().setNutricao2(checkNutricao2.isChecked());
        viewModel.getModel().setNutricao3(checkNutricao3.isChecked());
        viewModel.getModel().setNutricao4(checkNutricao4.isChecked());
        viewModel.getModel().setNutricao5(checkNutricao5.isChecked());
        viewModel.getModel().setNutricao6(checkNutricao6.isChecked());
    }

    private void setCheckComportamento(){
        viewModel.getModel().setComportamento1(checkComportamento1.isChecked());
        viewModel.getModel().setComportamento2(checkComportamento2.isChecked());
        viewModel.getModel().setComportamento3(checkComportamento3.isChecked());
        viewModel.getModel().setComportamento4(checkComportamento4.isChecked());
        viewModel.getModel().setComportamento5(checkComportamento5.isChecked());
    }

    private String getTipoAnimal(){
        RadioButton radioCao,radioGato,radioEquino;
        radioCao=findViewById(R.id.radioCao);
        radioGato=findViewById(R.id.radioGato);
        radioEquino=findViewById(R.id.radioEquino);

        if (radioCao.isChecked())
            return OrientacoesModel.CAO;
        if (radioGato.isChecked())
            return OrientacoesModel.GATO;
        if (radioEquino.isChecked())
            return OrientacoesModel.EQUINO;
        return "";
    }

    private void setDataModel(){
        setTextsModel();
        setCheckConfortoModel();
        setCheckSanidadeModel();
        setCheckNutricaoModel();
        setCheckComportamento();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setDataModel();
    }

    private AlertDialog.Builder getAlert(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        input= new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setMinLines(2);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(input);
        return builder;
    }

    public void btnNomeDoAnimal(View v){
        final View view=v;
        AlertDialog.Builder builder=
                getAlert("Adicionar Nome do animal","Adicione o nome do animal!");
        try{
            input.setText("");
            input.setText(viewModel.getModel().getNomeAnimal());
        }catch (Exception e){

        }
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nome= input.getText().toString();
                if(nome.replace(" ","").equals("") || nome==null){
                    Toast.makeText(OrientacoesActivity.this,
                            "O nome do responsável não pode estar vazio!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    OrientacoesActivity.this.viewModel.getModel().setNomeAnimal(nome);
                    ((Button)view).setText("Editar Nome do Animal");
                }
            }
        }).setNegativeButton("Cancelar",null).create().show();
    }

    public void btnProprietario(View v){
        final View view=v;
        AlertDialog.Builder builder=
                getAlert("Adicionar Proprietário","Separe os responsáveis por vírgulas!");
        try{
            input.setText("");
            input.setText(viewModel.getModel().getNomeProprietario());
        }catch (Exception e){

        }
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nome= input.getText().toString();
                if(nome.replace(" ","").equals("") || nome==null){
                    Toast.makeText(OrientacoesActivity.this,
                            "O nome do Proprietário não pode estar vazio!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    OrientacoesActivity.this.viewModel.getModel().setNomeProprietario(nome);
                    ((Button)view).setText("Editar do proprietário");
                }
            }
        }).setNegativeButton("Cancelar",null).create().show();
    }

    public void generatePdf(View v){
        if (pathAssinaturaOperacao==null){
            AlertDialog.Builder b=new AlertDialog.Builder(this);
            b.setTitle("Atenção")
                    .setMessage("A assinatura do responsável pela operação não foi preenchida, deseja continuar sem assinatura?")
                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setDataModel();
                            makePdf();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create().show();
            return;
        }
        if (pathAssinaturaResponsavel==null && !getCheckBoxIsChecked(R.id.checkAssinaturaProprietario)){
            AlertDialog.Builder b=new AlertDialog.Builder(this);
            b.setTitle("Atenção continuar sem assinatura?")
                    .setMessage("Você disse que o proprietário não se recusou assinar, mas a assinatura está vazia, deseja continuar sem assinatura?")
                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setDataModel();
                            makePdf();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create().show();
            return;
        }
        setDataModel();
        String a=PdfUtils.makeOrientacoes(viewModel.getModel(),this);
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
        StorageUtil.sendPdf(new File(a),this);
        //actionPdf(a,viewModel.getModel());
    }

    private void makePdf(){
        String a=PdfUtils.makeOrientacoes(viewModel.getModel(),OrientacoesActivity.this);
        Toast.makeText(OrientacoesActivity.this, a, Toast.LENGTH_SHORT).show();
        StorageUtil.sendPdf(new File(a),this);
    }

    private void actionPdf(String path, OrientacoesModel model){
        Intent i= new Intent(this,ActionPdfActivity.class);
        UrlsPdfModel urlModel=new UrlsPdfModel();
        urlModel.setN(model.getNumeroOcorrencia());
        urlModel.setDt(urlModel.getDt());
        urlModel.setEnd(model.getEndereco());
        i.putExtra(ActionPdfActivity.PATH,path);
        i.putExtra(ActionPdfActivity.TIPO,ActionPdfActivity.TYPE_ORIENTACOES);
        i.putExtra(ActionPdfActivity.TITLE,"nº "+model.getNumeroOcorrencia()+"\n"+model.getNomeProprietario());
        i.putExtra(ActionPdfActivity.MODEL,urlModel);
        startActivity(i);
    }

    public void assinaturaResponsavel(View v){
        Intent i=new Intent(this,AssinaturaActivity.class);
        startActivityForResult(i, ASSINATURA_RESPONSAVEL);
    }

    public void assinaturaOperacao(View v){
        Intent i=new Intent(this,AssinaturaActivity.class);
        startActivityForResult(i, ASSINATURA_OPERACAO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK) return;

        Bundle extras = data.getExtras();
        switch (requestCode){
            case ASSINATURA_RESPONSAVEL:
                if(extras!=null){
                    pathAssinaturaResponsavel=extras.getString("path");
                }
                break;
            case ASSINATURA_OPERACAO:
                if(extras!=null){
                    pathAssinaturaOperacao=extras.getString("path");
                }
                break;
        }
    }
}
