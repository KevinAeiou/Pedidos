package com.example.kevin.pedidos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActCadPedido extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNomeCliente, edtNomeProd, edtValor;
    private Button btnCancelar, btnConfirmar;
    private Spinner spinQuant;
    private RecyclerView rv;
    String[] menssagens = {"Preencha todos os campos","pedido cadastrado"};

    ArrayList<Pedido> listaPedidos;
    PedidoAdapter myAdapter;
    FirebaseFirestore db;
    String usuarioId, clienteId, clienteAtual;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cad_pedido);

        iniciaComponentes();

        //Seta o campo edtNomeCliente com o valor passado pelo evento de click
        edtNomeCliente.setText(clienteAtual);

        btnCancelar.setOnClickListener(this);
        btnConfirmar.setOnClickListener(this);

        ArrayAdapter<String> meuAdaptador = new ArrayAdapter<>(ActCadPedido.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.numeros));
        meuAdaptador.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinQuant.setAdapter(meuAdaptador);

        //RecyclerView
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        listaPedidos = new ArrayList<>();
        myAdapter = new PedidoAdapter(ActCadPedido.this, listaPedidos);
        rv.setAdapter(myAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db = FirebaseFirestore.getInstance();
        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Usuario/" + usuarioId + "/Clientes")
                .whereEqualTo("nomeCliente", clienteAtual)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                clienteId = doc.getId();
                                db.collection("Usuario/" + usuarioId + "/Clientes/" + clienteId + "/Pedidos")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                for (DocumentChange dc: value.getDocumentChanges()) {
                                                    if (error !=null){
                                                        Log.e("Firestore erro!", "erro!"+error.getMessage());
                                                        return;
                                                    }
                                                    if (dc.getType() == DocumentChange.Type.ADDED){
                                                        listaPedidos.add(dc.getDocument().toObject(Pedido.class));
                                                    }
                                                    myAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d("cli", "Erro ao buscar clientes", task.getException());
                        }
                    }
                });
        /*
        db.collection("Usuario/" + usuarioId + "/Clientes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error !=null){
                            Log.e("Firestore erro!", "erro!"+error.getMessage());
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            if (doc.get("nomeCliente") == clienteAtual){
                                Log.d("cli", "Cliente encontrado!");
                                clienteId = doc.getId();
                                db.collection("Usuario/" + usuarioId + "/Clientes/" + clienteId + "/Pedidos")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                for (DocumentChange dc: value.getDocumentChanges()) {
                                                    if (error !=null){
                                                        Log.e("Firestore erro!", "erro!"+error.getMessage());
                                                        return;
                                                    }
                                                    if (dc.getType() == DocumentChange.Type.ADDED){
                                                        listaPedidos.add(dc.getDocument().toObject(Pedido.class));
                                                    }
                                                    myAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
         */
    }

    private void iniciaComponentes() {
        edtNomeCliente = findViewById(R.id.edtCliente);
        edtNomeProd = findViewById(R.id.edtNomeProduto);
        edtValor = findViewById(R.id.edtValor);
        btnCancelar = findViewById(R.id.btnCancPedido);
        btnConfirmar = findViewById(R.id.btnConfPedido);
        spinQuant = findViewById(R.id.edtQuant);
        rv = findViewById(R.id.rv_pedidos);
        intent = getIntent();
        clienteAtual = intent.getExtras().getString("Cliente");
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
            salvarCliente();
            //salvarPedido();
            startActivity(new Intent(this, ActMain.class));
            finish();
        }

    }

    private boolean verificaCampos(String nomeCli, String nomeProd, String valor) {
        if (nomeCli.isEmpty()||nomeProd.isEmpty()||valor.isEmpty())
            return false;
        return true;
    }

    private void salvarCliente() {

        String nomeCli = edtNomeCliente.getText().toString();

        String nomeProd = edtNomeProd.getText().toString();
        Double valor = Double.parseDouble(edtValor.getText().toString());
        int quant = spinQuant.getSelectedItemPosition();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> usuario = new HashMap<>();
        Map<String, Object> pedidos = new HashMap<>();

        usuario.put("nomeCliente", nomeCli);

        pedidos.put("nomeProduto", nomeProd);
        pedidos.put("valor", valor);
        pedidos.put("quantidade", quant+1);

        db.collection("Usuario/"+usuarioId+"/Clientes")
                .add(usuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("cli", "DocumentSnapshot written with ID: " + documentReference.getId());
                        clienteId = documentReference.getId();
                        Log.d("cli", clienteId);
                        db.collection("Usuario/"+usuarioId+"/Clientes/" + clienteId + "/Pedidos")
                                .add(pedidos);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("cli", "Error adding document", e);
                    }
                });

    }

    private void salvarPedido() {

        String nomeProd = edtNomeProd.getText().toString();
        Double valor = Double.parseDouble(edtValor.getText().toString());
        int quant = spinQuant.getSelectedItemPosition();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> pedidos = new HashMap<>();

        pedidos.put("nomeProduto", nomeProd);
        pedidos.put("valor", valor);
        pedidos.put("quantidade", quant+1);

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Usuario/"+usuarioId+"/Clientes/" + clienteId + "/Pedidos")
                .add(pedidos);
    }
}