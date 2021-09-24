package com.example.kevin.pedidos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ActLogin extends AppCompatActivity implements View.OnClickListener {

    private TextView txtEsqSenha, txtCadastro;
    private EditText edtEmail, edtSenha;
    private Button btnLogar;
    String [] menssagens = {"Preencha todos os campos!", "Login efetuado com sucesso!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_login);

        iniciarComponentes();

        txtCadastro.setOnClickListener(this);
        btnLogar.setOnClickListener(this);
    }

    private void iniciarComponentes() {
        txtEsqSenha = findViewById(R.id.txtEsqSenha);
        txtCadastro = findViewById(R.id.txtLinkCasdastro);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogar = findViewById(R.id.btnLogar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtLinkCasdastro:
                startActivity(new Intent(this, ActCadUsuario.class));
                break;
            case R.id.btnLogar:
                loginUsuario(view);
        }
    }

    private void loginUsuario(View view) {
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (email.isEmpty() || senha.isEmpty()){
            Snackbar snackbar = Snackbar.make(view, menssagens[0], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }else {
            autenticarUser(view);
        }
    }

    private void autenticarUser(View view) {
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    new Handler().postDelayed(() ->{
                            telaPrincipal();
                    }, 3000);
                }else {
                    String erro;

                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao logar!";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void telaPrincipal() {
        Intent intent = new Intent(ActLogin.this, ActMain.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null){
            telaPrincipal();
        }
    }
}