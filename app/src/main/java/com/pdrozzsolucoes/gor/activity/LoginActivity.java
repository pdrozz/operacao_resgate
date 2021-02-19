package com.pdrozzsolucoes.gor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.pdrozzsolucoes.gor.R;
import com.pdrozzsolucoes.gor.helper.ViewMethodsHelper;
import com.pdrozzsolucoes.gor.utils.auth.AuthUtils;
import com.pdrozzsolucoes.gor.utils.preferences.MyPreferences;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmailLogin,editSenhaLogin;
    private Button btnEntrarLogin;
    private ProgressBar progressBar;
    private MyPreferences myPreferences;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myPreferences=new MyPreferences(this);
        configWidgets();

        setBackgroundImage();

    }

    private void setBackgroundImage(){
        int image=new Random().nextInt(3);
        switch (image){
            case 0:
                backgroundImage.setImageResource(R.drawable.bg_login);
                break;
            case 1:
                backgroundImage.setImageResource(R.drawable.bg_login1);
                break;
            case 2:
                backgroundImage.setImageResource(R.drawable.bg_login2);
                break;
            default:
                break;
        }
    }

    public void entrar(View v){
        if(!validateEmail() || !validatePassword())
            return;

        ViewMethodsHelper.toggleVisibility(progressBar,View.INVISIBLE);
        btnEntrarLogin.setEnabled(false);

        final String email=editEmailLogin.getText().toString();
        final String senha=editSenhaLogin.getText().toString();
        AuthUtils.login(email,senha)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Sucesso ao entrar!", Toast.LENGTH_SHORT).show();

                        myPreferences.savePref(MyPreferences.emailUser,email);
                        myPreferences.savePref(MyPreferences.passUser,senha);


                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO THROW EXCEPTION AND SHOW DIFFERENT MESSAGE
                Toast.makeText(LoginActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                ViewMethodsHelper.toggleVisibility(progressBar);
                btnEntrarLogin.setEnabled(true);
            }
        });

    }

    private boolean validatePassword(){
        String pass=editSenhaLogin.getText().toString();
        if(pass==""){
            Toast.makeText(this, "A senha precisa ser preenchida!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean validateEmail(){
        String email=editEmailLogin.getText().toString();

        if (email=="" || email.replace(" ","")=="" || email==null
        || !email.contains("@") || !email.contains(".")
        ){
            Toast.makeText(this, "Insira um email valido!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void configWidgets(){
        editEmailLogin=findViewById(R.id.editEmailLogin);
        editSenhaLogin=findViewById(R.id.editSenhaLogin);
        btnEntrarLogin=findViewById(R.id.btnEntrarLogin);
        progressBar=findViewById(R.id.progress);
        backgroundImage=findViewById(R.id.backgroundImage);
    }
}
