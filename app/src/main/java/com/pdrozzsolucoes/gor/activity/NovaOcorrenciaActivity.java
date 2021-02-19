package com.pdrozzsolucoes.gor.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.model.novaOcorrencia.NovaOcorrenciaModel;
import com.pdrozzsolucoes.gor.utils.MaskText;
import com.pdrozzsolucoes.gor.utils.pdf.PdfUtils;
import com.pdrozzsolucoes.gor.utils.storage.StorageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

public class NovaOcorrenciaActivity extends AppCompatActivity {

    private EditText numeroOcorrencia, editGerada, editFinalizada, editTipoOcorrencia, editOrigem,
            editEndereco, editDesc, editTipoAnimal, editQualAnimal, editTelefone;
    private Button btnAdicionarResponsavel, btnAdicionarVoluntario, btnAnexarImagem, btnImagensAnexadas, btnGerarPdf;
    private Spinner spinnerTipoOcorrencia, spinnerOrigem;
    private CheckBox checkedTipoOcorrencia, checkedOrigemOutro;

    private NovaOcorrenciaModel model;

    private EditText input;

    private String[] tipoOcorrencia = {"Denúncia de Maus-tratos"};
    private String[] origemOcorrencia = {"Policia Militar Ambiental/SC",
            "Polícia Militar/SC",
            "Corpo de Bombeiros Militar/SC",
            "Defesa Civil",
            "Polícia Civil",
            "Prefeitura de Itapema",
            "Prefeitura de Porto Belo",
            "Prefeitura de Tijucas",
            "Prefeitura de Bombinhas",
            "Prefeitura de Camboriú",
            "FUCAM - Fund. do Meio Ambiente de Camboriú",
            "FAACI - Fund. do Meio Ambiente de Itapema",
            "FAMAB - Fund. do Meio Ambiente de Bombinhas",
            "FAMAP - Fund. do Meio Ambiente de Porto Belo",
            "CIDASC",
            "Auto Pista Litoral Sul"
    };

    private ArrayAdapter origemAdapter, tipoOcorrenciaAdapter;

    private final int REQUEST_IMAGE_CAPTURE = 300;
    private final int GALLERY_REQUEST_CODE = 350;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_ocorrencia);
        configWidgets();
        if (model == null) {
            model = new NovaOcorrenciaModel();
        }
        configTextWatcher();
        configSpinner();
        configCheckboxListener();

        try {
            limparAnexos();
        } catch (Exception e) {
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || false) {
                    limparAnexos();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
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
            } else {
                limparAnexos();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ocorreu um erro conceda as permissões do app", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparAnexos() {
        //ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = StorageUtil.getAnexoPath(this);
        directory.delete();
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 230);
            }
        }
    }

    private void makeModel() {
        if (!numeroOcorrencia.getText().toString().equals("")) {
            model.setNumeroOcorrencia(numeroOcorrencia.getText().toString());
        } else {
            model.setNumeroOcorrencia(new Random().nextInt(200000) + "/" + new Random().nextInt(6000));
        }
        if (editGerada.getText().toString() != null) {
            model.setGerada(editGerada.getText().toString());
        }
        if (editFinalizada.getText().toString() != null) {
            model.setFinalizada(editFinalizada.getText().toString());
        }
        if (!getTipoOcorrencia().equals("")) {
            model.setTipoOcorrencia(getTipoOcorrencia());
        }
        if (!getOrigem().equals("")) {
            model.setOrigemOcorrencia(getOrigem());
        }
        if (editEndereco.getText().toString() != null) {
            model.setEndereco(editEndereco.getText().toString());
        }
        if (editDesc.getText().toString() != null) {
            model.setDescricao(editDesc.getText().toString());
        }
        if (editTipoAnimal.getText().toString() != null) {
            model.setTipoAnimal(editTipoAnimal.getText().toString());
        }
        if (editTelefone.getText().toString() != null) {
            model.setTelefone(editTelefone.getText().toString());
        }
    }

    private String getOrigem() {
        if (checkedOrigemOutro.isChecked()) {
            return editOrigem.getText().toString();
        }
        return spinnerOrigem.getSelectedItem().toString();
    }

    private String getTipoOcorrencia() {
        if (checkedTipoOcorrencia.isChecked()) {
            return editTipoOcorrencia.getText().toString();
        }
        return spinnerTipoOcorrencia.getSelectedItem().toString();
    }

    private void configCheckboxListener() {
        checkedOrigemOutro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    spinnerOrigem.setEnabled(false);
                    editOrigem.setEnabled(true);
                } else {
                    spinnerOrigem.setEnabled(true);
                    editOrigem.setEnabled(false);
                }
            }
        });
        checkedTipoOcorrencia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    spinnerTipoOcorrencia.setEnabled(false);
                    editTipoOcorrencia.setEnabled(true);
                } else {
                    spinnerTipoOcorrencia.setEnabled(true);
                    editTipoOcorrencia.setEnabled(false);
                }
            }
        });
    }

    public void showAlertDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Como desejar anexar as imagens?")
                .setMessage("Deseja usar a câmera ou a galeria?")
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

    public void verImagensAnexadas(View v) {
        startActivity(new Intent(this, ImagensAnexadasActivity.class));
    }

    private void takePictureCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("anexoO1S", Context.MODE_PRIVATE);
        // path to /data/data/yourapp/app_data/imageDir
        if (!directory.exists()) {
            directory.mkdir();
        }
        String fileName=UUID.randomUUID().toString().replace("-", "") + ".png";
        File mypath = new File(directory, fileName);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mypath.getAbsolutePath());
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void takePhoto() {
        File anexoPath = StorageUtil.getAnexoPath(this);

        String fileName=UUID.randomUUID().toString().replace("-", "") + ".png";

        String authory = "com.pdrozzsolucoes.fileprovider";
        File tempFile = new File(anexoPath, fileName);
        FileProvider.getUriForFile(NovaOcorrenciaActivity.this, authory, tempFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(NovaOcorrenciaActivity.this, authory, tempFile));
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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


    private void configSpinner() {
        origemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, origemOcorrencia);
        tipoOcorrenciaAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoOcorrencia);
        origemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoOcorrenciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrigem.setAdapter(origemAdapter);
        spinnerTipoOcorrencia.setAdapter(tipoOcorrenciaAdapter);
    }

    private void configTextWatcher() {
        numeroOcorrencia.addTextChangedListener(MaskText.mask(numeroOcorrencia, MaskText.FORMAT_OCORRENCIA));
        editGerada.addTextChangedListener(MaskText.mask(editGerada, MaskText.FORMAT_HOUR));
        editFinalizada.addTextChangedListener(MaskText.mask(editFinalizada, MaskText.FORMAT_HOUR));
        editTelefone.addTextChangedListener(MaskText.mask(editTelefone, MaskText.FORMAT_FONE));
    }

    private AlertDialog.Builder getAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        input = new EditText(this);
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

    public void btnResponsavel(View v) {
        final View view = v;
        AlertDialog.Builder builder =
                getAlert("Adicionar Responsável", "Separe os responsáveis por vírgulas!");
        try {
            input.setText("");
            input.setText(model.getResponsavel());
        } catch (Exception e) {

        }
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String responsavel = input.getText().toString();
                if (responsavel.replace(" ", "").equals("") || responsavel == null) {
                    Toast.makeText(NovaOcorrenciaActivity.this,
                            "O nome do responsável não pode estar vazio!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    NovaOcorrenciaActivity.this.model.setResponsavel(responsavel);
                    ((Button) view).setText("Editar Responsável");
                }
            }
        }).setNegativeButton("Cancelar", null).create().show();
    }

    public void btnVoluntario(View v) {
        final View view = v;
        AlertDialog.Builder builder =
                getAlert("Adicionar Voluntários(as)", "Separe os voluntários por vírgulas!");
        try {
            input.setText("");
            input.setText(model.getVonluntaio());
        } catch (Exception e) {

        }
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String voluntario = input.getText().toString();
                if (voluntario.replace(" ", "").equals("") || voluntario == null) {
                    Toast.makeText(NovaOcorrenciaActivity.this,
                            "O nome do voluntário não pode estar vazio!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    NovaOcorrenciaActivity.this.model.setVonluntaio(voluntario);
                    ((Button) view).setText("Editar voluntário(a)");
                }
            }
        }).setNegativeButton("Cancelar", null).create().show();
    }

    public void genereratePdf(View v) {
        if (editEndereco.getText().toString().equals("")) {
            Toast.makeText(this, "Preencha o endereço!", Toast.LENGTH_SHORT).show();
            return;
        }
        makeModel();
        String s = PdfUtils.makeNovaOcorrência(model, this);
        model.setUrlPdf(s);
        Intent i = new Intent(this, ActionWithPdfActivity.class);
        i.putExtra(ActionWithPdfActivity.KEY_MODEL, model);
        startActivity(i);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void configWidgets() {
        numeroOcorrencia = findViewById(R.id.numeroOcorrencia);
        editGerada = findViewById(R.id.editGerada);
        editFinalizada = findViewById(R.id.editFinalizada);
        editOrigem = findViewById(R.id.editOrigem);
        editEndereco = findViewById(R.id.editEndereco);
        editDesc = findViewById(R.id.editDesc);
        editTipoAnimal = findViewById(R.id.editTipoAnimal);
        editQualAnimal = findViewById(R.id.editQualAnimal);
        editTelefone = findViewById(R.id.editTelefone);
        editTipoOcorrencia = findViewById(R.id.editTipoOcorrencia);

        numeroOcorrencia = findViewById(R.id.numeroOcorrencia);
        btnAdicionarVoluntario = findViewById(R.id.btnAdicionarVoluntario);
        btnAnexarImagem = findViewById(R.id.btnAnexarImagem);
        btnImagensAnexadas = findViewById(R.id.btnImagensAnexadas);
        btnGerarPdf = findViewById(R.id.btnGerarPdf);

        spinnerTipoOcorrencia = findViewById(R.id.spinnerTipoOcorrencia);
        spinnerOrigem = findViewById(R.id.spinnerOrigem);

        checkedTipoOcorrencia = findViewById(R.id.checkedTipoOcorrencia);
        checkedOrigemOutro = findViewById(R.id.checkedOrigemOutro);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;
        File directory = StorageUtil.getAnexoPath(this);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                // path to /data/data/yourapp/app_data/imageDir
                String TAG="got";
                Log.i(TAG, "onActivityResult: REQUEST IMAGE");
                // Create imageDir
                //File mypath = new File(directory, UUID.randomUUID().toString().replace("-", "") + ".png");
                //FileOutputStream fos = null;
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
                // path to /data/data/yourapp/app_data/imageDir
                if (!directory.exists()) {
                    directory.mkdir();
                }
                // Create imageDir
                File mypaths = new File(directory, UUID.randomUUID().toString().replace("-", "") + ".jpeg");
                FileOutputStream foss = null;
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
