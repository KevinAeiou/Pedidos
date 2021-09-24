package com.example.kevin.pedidos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ActCadUsuario extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNome, edtEmail, edtSenha;
    private Button btnCadastrar;
    private TextView txtEntrar;
    String[] menssagens = {"Preencha todos os campos", "Usuário cadastrado com sucesso!"};
    String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cad_usuario);

        iniciarComponentes();

        txtEntrar.setOnClickListener(this);
        btnCadastrar.setOnClickListener(this);
    }

    private void iniciarComponentes() {
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        txtEntrar = findViewById(R.id.txtLinkEntrar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtLinkEntrar:
                startActivity(new Intent(this, ActLogin.class));
                break;
            case R.id.btnCadastrar:
                cadastrarUsuario(view);
        }
    }

    private void cadastrarUsuario(final View v) {
        String nome = edtNome.getText().toString();
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (!verificaCampos(nome, email, senha)){
            Snackbar snackbar = Snackbar.make(v, menssagens[0], Snackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
        }else  {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        salvarDadosUsuario();
                        startActivity(new Intent(ActCadUsuario.this, ActLogin.class));
                        finish();
                    }else{
                        String erro;
                        try{
                            throw task.getException();
                        }catch (FirebaseAuthWeakPasswordException e){
                            erro = "A senha deve conter no mínimo 8 caracteres!";
                        } catch (FirebaseAuthUserCollisionException e){
                            erro = "Conta já cadastrada!";
                        }catch (FirebaseAuthInvalidCredentialsException e) {
                            erro = "Email inválido!";
                        }catch (Exception e) {
                            erro = "Erro ao cadastrar usuário";
                        }

                        Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }
            });
        }
    }

    private void salvarDadosUsuario() {
        String nome  = edtNome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map <String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuario").document(usuarioId);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("db", "Sucesso!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db", "Erro ao salvar!" + e.toString());
            }
        });
    }

    private boolean verificaCampos(String nome, String email, String senha) {
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
            return false;
        }
        return true;
    }
}