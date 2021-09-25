package com.example.kevin.pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ActCadPedido extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNomeCliente, edtNomeProd, edtValor;
    private Button btnCancelar, btnConfirmar;
    private Spinner spinQuant;
    String[] menssagens = {"Preencha todos os campos","pedido cadastrado"};
    String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cad_pedido);

        iniciaComponentes();

        btnCancelar.setOnClickListener(this);
        btnConfirmar.setOnClickListener(this);

        ArrayAdapter<String> meuAdaptador = new ArrayAdapter<>(ActCadPedido.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.numeros));
        meuAdaptador.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinQuant.setAdapter(meuAdaptador);
    }

    private void iniciaComponentes() {
        edtNomeCliente = findViewById(R.id.edtCliente);
        edtNomeProd = findViewById(R.id.edtNomeProduto);
        edtValor = findViewById(R.id.edtValor);
        btnCancelar = findViewById(R.id.btnCancPedido);
        btnConfirmar = findViewById(R.id.btnConfPedido);
        spinQuant = findViewById(R.id.edtQuant);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancPedido:
                startActivity(new Intent(this, ActMain.class));
                break;
            case R.id.btnConfPedido:
                verificaPedido(v);
        }
    }

    private void verificaPedido(final View v) {
        String nomeCli = edtNomeCliente.getText().toString();
        String nomeProd = edtNomeProd.getText().toString();
        String valor = edtValor.getText().toString();

        if (!verificaCampos(nomeCli, nomeProd, valor)){
            Snackbar snackbar = Snackbar.make(v, menssagens[0], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }else{
            salvarPedido();
            startActivity(new Intent(this, ActMain.class));
            finish();
        }

    }

    private boolean verificaCampos(String nomeCli, String nomeProd, String valor) {
        if (nomeCli.isEmpty()||nomeProd.isEmpty()||valor.isEmpty())
            return false;
        return true;
    }

    private void salvarPedido() {
        String nomeCli = edtNomeCliente.getText().toString();
        String nomeProd = edtNomeProd.getText().toString();
        Double valor = Double.parseDouble(edtValor.getText().toString());
        int quant = spinQuant.getSelectedItemPosition();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> pedidos = new HashMap<>();
        pedidos.put("nomeCliente", nomeCli);
        pedidos.put("nomeProduto", nomeProd);
        pedidos.put("valor", valor);
        pedidos.put("quantidade", quant+1);

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuario/"+usuarioId+"/Pedidos").document();
        documentReference.set(pedidos);
    }

}