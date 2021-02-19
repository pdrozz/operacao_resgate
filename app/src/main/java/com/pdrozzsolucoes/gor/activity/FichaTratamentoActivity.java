package com.pdrozzsolucoes.gor.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.adapter.MedicamentoAdapter;
import com.pdrozzsolucoes.gor.helper.ViewMethodsHelper;
import com.pdrozzsolucoes.gor.model.UrlsPdfModel;
import com.pdrozzsolucoes.gor.model.fichaTratamento.FichaTratamentoModel;
import com.pdrozzsolucoes.gor.model.fichaTratamento.FichaTratamentoViewModel;
import com.pdrozzsolucoes.gor.model.fichaTratamento.MedicamentoModel;
import com.pdrozzsolucoes.gor.utils.MaskText;
import com.pdrozzsolucoes.gor.utils.pdf.PdfUtils;
import com.pdrozzsolucoes.gor.utils.storage.StorageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FichaTratamentoActivity extends AppCompatActivity {

    private EditText numeroFicha,editEquina,editPelagem,editCaracteristicas,
            editDataInicio,editSuspeitaClinica,editProcedimentos,editMedicamento,
            editQuantidade,editDose,editVia,editDuracao,editPeriodo,editDataAlta,
            editDataObito,editCausa,editDestino,editDataDestino;


    private String pathAnexo="";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private final int GALLERY_REQUEST_CODE=34;
    private final int REQUEST_IMAGE_CAPTURE=324;

    private LinearLayout linearObito,linearEquino,linearDestino;

    private FichaTratamentoModel model;
    private FichaTratamentoViewModel viewModel;
    private List<MedicamentoModel> listMedicamentos=new ArrayList<>();

    private MedicamentoAdapter adapter;

    private String pathAssinatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_tratamento);


        configWidgets();
        configRadioListener();
        setTextMask();

        viewModel=new ViewModelProvider(this).get(FichaTratamentoViewModel.class);
        setDataInView(viewModel);
        adapter=new MedicamentoAdapter(listMedicamentos,this);
        configRecycler(adapter);
    }

    private boolean validate(EditText edit){
        String s=edit.getText().toString();
        if(s.equals("") || s.replace(" ","").equals(""))
            return false;

        return true;
    }

    public void adicionarMedicamento(View v){
        if(!validate(editMedicamento)){
            Toast.makeText(this, "O nome do medicamento não pode estar vazio!", Toast.LENGTH_LONG).show();
            return;
        }
        if(!validate(editQuantidade)){
            Toast.makeText(this, "A quantidade do medicamento não pode estar vazio!", Toast.LENGTH_LONG).show();
            return;
        }

        MedicamentoModel medicamentoModel=new MedicamentoModel();
        medicamentoModel.setMedicamento(editMedicamento.getText().toString());
        medicamentoModel.setQnt(editQuantidade.getText().toString());
        medicamentoModel.setDose(editDose.getText().toString());
        medicamentoModel.setVia(editVia.getText().toString());
        medicamentoModel.setDuração(editDuracao.getText().toString());
        medicamentoModel.setPeríodo(editPeriodo.getText().toString());

        limparEditMedicamentos();

        listMedicamentos.add(medicamentoModel);
        configRecycler(new MedicamentoAdapter(listMedicamentos,this));
    }

    private void limparEditMedicamentos(){
        editMedicamento.setText("");
        editQuantidade.setText("");
        editDose.setText("");
        editVia.setText("");
        editPeriodo.setText("");
        editDuracao.setText("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        setDataInViewModel();
    }

    private void setDataInViewModel(){
        FichaTratamentoModel model=viewModel.getModel();

        model.setNumeroFicha(numeroFicha.getText().toString());
        model.setPelagem(editPelagem.getText().toString());
        model.setCaract(editCaracteristicas.getText().toString());
        model.setDataInicio(editDataInicio.getText().toString());
        model.setSuspeitaClinica(editSuspeitaClinica.getText().toString());
        model.setProcedimentos(editProcedimentos.getText().toString());
        model.setDataAlta(editDataAlta.getText().toString());
        model.setDataObito(editDataObito.getText().toString());
        model.setCausaObito(editCausa.getText().toString());
        model.setSexo(getSexo());
        model.setDestino(getDestino());
        model.setOutroDestino(getRadioIsChecked(R.id.radioOutro));
        model.setListItem(adapter.getListMedicamento());
        model.setDataDestino(editDataDestino.getText().toString());

        model.setObito(getRadioIsChecked(R.id.radioSim));
        model.setEquino(getRadioIsChecked(R.id.radioEquino));
        model.setEspecie(getEspecie());

        viewModel.setModel(model);
    }

    private String getDestino(){
        if(getRadioIsChecked(R.id.radioOutro)){
            return editDestino.getText().toString();
        }
        if(getRadioIsChecked(R.id.radioDoacao)){
            return FichaTratamentoViewModel.DOACAO;
        }
        if(getRadioIsChecked(R.id.radioRetorno)) {
            return FichaTratamentoViewModel.RETORNO;
        }
        return "Não especificado.";
    }

    private void setCheckRadio(int id,boolean b){
        RadioButton radioButton=findViewById(id);
        radioButton.setChecked(b);
    }

    private void setDataInView(FichaTratamentoViewModel viewModel){
        FichaTratamentoModel model=viewModel.getModel();

        numeroFicha.setText(model.getNumeroFicha());
        editPelagem.setText(model.getNumeroFicha());
        editCaracteristicas.setText(model.getNumeroFicha());
        editDataInicio.setText(model.getNumeroFicha());
        editSuspeitaClinica.setText(model.getNumeroFicha());
        editProcedimentos.setText(model.getNumeroFicha());
        editDataAlta.setText(model.getNumeroFicha());
        editDataObito.setText(model.getNumeroFicha());

        switch (model.getEspecie()){
            case FichaTratamentoViewModel.CAO:
                setCheckRadio(R.id.radioCao,true);
                break;
            case FichaTratamentoViewModel.GATO:
                setCheckRadio(R.id.radioCao,true);
                break;
            case FichaTratamentoViewModel.EQUINO:
                setCheckRadio(R.id.radioCao,true);
                break;
            default:
                break;
        }

        switch (model.getSexo()){
            case FichaTratamentoViewModel.MACHO:
                setCheckRadio(R.id.radioMacho,true);
                break;
            case FichaTratamentoViewModel.FEMEA:
                setCheckRadio(R.id.radioFemea,true);
                break;
            default:
                break;
        }

        if(model.getObito()){
            setCheckRadio(R.id.radioSim,true);
            editCausa.setText(model.getCausaObito());
        }
    }

    private String getSexo(){
        if (getRadioIsChecked(R.id.radioFemea))
            return FichaTratamentoViewModel.FEMEA;
        if (getRadioIsChecked(R.id.radioMacho))
            return FichaTratamentoViewModel.MACHO;
        return "Não especificada";
    }

    private String getEspecie(){
        if (getRadioIsChecked(R.id.radioGato))
            return FichaTratamentoViewModel.GATO;
        if (getRadioIsChecked(R.id.radioCao))
            return FichaTratamentoViewModel.CAO;
        if(getRadioIsChecked(R.id.radioEquino)){
            return editEquina.getText().toString();
        }
        return "Não especificado.";
    }

    private boolean getRadioIsChecked(int id){
        RadioButton radioButton=findViewById(id);
        return radioButton.isChecked();
    }

    private void setTextMask(){
        editDataAlta.addTextChangedListener(MaskText.mask(editDataAlta,MaskText.FORMAT_DATE));
        editDataInicio.addTextChangedListener(MaskText.mask(editDataInicio,MaskText.FORMAT_DATE));
        editDataObito.addTextChangedListener(MaskText.mask(editDataObito,MaskText.FORMAT_DATE));
        editDataDestino.addTextChangedListener(MaskText.mask(editDataDestino,MaskText.FORMAT_DATE));
    }

    private void configRadioListener(){
        RadioButton equino=findViewById(R.id.radioEquino);
        equino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ViewMethodsHelper.toggleVisibility(linearEquino);
            }
        });
        RadioButton obito=findViewById(R.id.radioSim);
        obito.setOnCheckedChangeListener((compoundButton, b)
                -> ViewMethodsHelper.toggleVisibility(linearObito));

        RadioButton destino=findViewById(R.id.radioOutro);
        destino.setOnCheckedChangeListener((compoundButton, b)
                -> ViewMethodsHelper.toggleVisibility(linearDestino));
    }

    private void configRecycler(MedicamentoAdapter adapter){
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void configWidgets(){
        numeroFicha=findViewById(R.id.numeroFicha);
        editEquina=findViewById(R.id.editEquina);
        editPelagem=findViewById(R.id.editPelagem);
        editCaracteristicas=findViewById(R.id.editCaracteristicas);
        editDataInicio=findViewById(R.id.editDataInicio);
        editSuspeitaClinica=findViewById(R.id.editSuspeitaClinica);
        editProcedimentos=findViewById(R.id.editProcedimentos);
        editMedicamento=findViewById(R.id.editMedicamento);
        editQuantidade=findViewById(R.id.editQuantidade);
        editDose=findViewById(R.id.editDose);
        editVia=findViewById(R.id.editVia);
        editDuracao=findViewById(R.id.editDuracao);
        editPeriodo=findViewById(R.id.editPeriodo);
        editDataObito=findViewById(R.id.editDataObito);
        editCausa=findViewById(R.id.editCausa);
        editDataAlta=findViewById(R.id.editDataAlta);
        recyclerView=findViewById(R.id.recycler);
        editDestino=findViewById(R.id.editDestino);
        editDataDestino=findViewById(R.id.editDataDestino);

        linearObito=findViewById(R.id.linearObito);
        linearEquino=findViewById(R.id.linearEquino);
        linearDestino=findViewById(R.id.linearDestino);

    }


    public void generatePdf(View v){
        if (numeroFicha.getText().toString().equals("")) {
            Toast.makeText(this, "Preencha o número da ficha", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pathAssinatura==null){
            AlertDialog.Builder b=new AlertDialog.Builder(this);
            b.setTitle("Atenção")
                    .setMessage("A assinatura não foi preenchida, deseja continuar sem assinatura?")
                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setDataInViewModel();
                            String a=PdfUtils.makeFichaTratamento(viewModel.getModel(),FichaTratamentoActivity.this,null);
                            Toast.makeText(FichaTratamentoActivity.this, a, Toast.LENGTH_SHORT).show();
                            actionPdf(a,viewModel.getModel());
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create().show();
        }else{
            String a=PdfUtils.makeFichaTratamento(viewModel.getModel(),FichaTratamentoActivity.this,pathAssinatura);
            Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
            actionPdf(a,viewModel.getModel());
        }
    }

    public void excluirAnexo(View v){
        if (pathAnexo==null || pathAnexo.equals("")){
            Toast.makeText(this, "Anexe uma imagem primeiro", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir?")
                .setMessage("Deseja excluir a imagem em anexo?" +
                        " Essa ação não pode ser desfeita")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean success=StorageUtil.getFichaAnexo(FichaTratamentoActivity.this).delete();
                        if (success)
                            Toast.makeText(FichaTratamentoActivity.this, "Anexo removido!", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }

    public void anexrImagemAnimal(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Como desejar anexar as imagens?")
                .setMessage("Deseja usar a câmera ou a galeria? Obs: Você só pode anexar uma imagem, " +
                        "se anexar outra estará substituindo a antiga")
                .setNegativeButton("Câmera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAlertCamera();
                    }
                })
                .setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pickFromGallery();
                    }
                }).create().show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Conceda as permissões", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertCamera() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção")
                .setMessage("É recomendado tirar as fotos no modo landscape (\"celular deitado\") para uma " +
                        "melhor edição no PDF!")
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        takePhoto();
                        //takePictureCamera();
                    }
                }).setCancelable(false).create().show();
    }



    private void takePhoto() {
        File anexoPath = StorageUtil.getFichaAnexo(this);

        String fileName= "anexo" + ".png";

        String authory = "com.pdrozzsolucoes.fileprovider";
        File tempFile = new File(anexoPath, fileName);
        FileProvider.getUriForFile(FichaTratamentoActivity.this, authory, tempFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(FichaTratamentoActivity.this, authory, tempFile));
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    public void verImagemAnexada(View v){
        if (!new File(StorageUtil.getFichaAnexo(this),"anexo.png").exists()){
            Toast.makeText(this, "Anexe uma imagem primeiro", Toast.LENGTH_LONG).show();
            return;
        }

        Intent i=new Intent(this,ImagemViewActivity.class);
        i.putExtra("path",pathAnexo);

        startActivity(i);
    }

    private void actionPdf(String path,FichaTratamentoModel model){
        Intent i= new Intent(this,ActionPdfActivity.class);
        UrlsPdfModel urlModel=new UrlsPdfModel();
        urlModel.setN(model.getNumeroFicha());
        urlModel.setDt(urlModel.getDt());
        i.putExtra(ActionPdfActivity.PATH,path);
        i.putExtra(ActionPdfActivity.TIPO,ActionPdfActivity.TYPE_FICHA);
        i.putExtra(ActionPdfActivity.TITLE,"Ficha nº "+model.getNumeroFicha());
        i.putExtra(ActionPdfActivity.MODEL,urlModel);
        startActivity(i);
    }

    public void assinatura(View v){
        Intent i=new Intent(this,AssinaturaActivity.class);
        startActivityForResult(i, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;

        switch (requestCode) {
            case 200:
                Bundle extras = data.getExtras();
                if(extras!=null){
                    pathAssinatura=extras.getString("path");
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                pathAnexo=StorageUtil.getFichaAnexo(this).getAbsolutePath()+"anexo.png";
                try {
                    //  fos = new FileOutputStream(mypath);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    // imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Ocorreu um erro ao anexar a imagem", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_REQUEST_CODE:
                // Create imageDir
                File mypaths = new File(StorageUtil.getFichaAnexo(this),
                        "anexo" + ".png");
                FileOutputStream foss = null;
                pathAnexo=StorageUtil.getFichaAnexo(this).getAbsolutePath()+"anexo.png";
                try {
                    foss = new FileOutputStream(mypaths);
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap bitmapImage = BitmapFactory.decodeStream(imageStream);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, foss);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Ocorreu um erro ao anexar a imagem", Toast.LENGTH_SHORT).show();
                } finally {
                    try {
                        foss.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }
}
