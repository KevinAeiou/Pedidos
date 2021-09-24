package com.example.kevin.pedidos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActCadPedido extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNomeCliente, edtNomeProd, edtValor, edtQuant;
    private Button btnCancelar, btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cad_pedido);

        iniciaComponentes();

        btnCancelar.setOnClickListener(this);
    }

    private void iniciaComponentes() {
        edtNomeCliente = findViewById(R.id.edtNome);
        edtNomeProd = findViewById(R.id.edtNomeProduto);
        edtValor = findViewById(R.id.edtValor);
        edtQuant = findViewById(R.id.edtQuant);
        btnCancelar = findViewById(R.id.btnCancPedido);
        btnConfirmar = findViewById(R.id.btnConfPedido);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancPedido:
                startActivity(new Intent(this, ActMain.class));
                break;
            case R.id.btnConfPedido:
        }
    }
}