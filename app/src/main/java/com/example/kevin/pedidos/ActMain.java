package com.example.kevin.pedidos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActMain extends AppCompatActivity {

    private TextView txtNome, txtDeslogar;
    private RecyclerView rv;

    //Teste de recyclerview
    ArrayList<Cliente> listaClientes;
    ClienteAdapter myAdapter;
    FirebaseFirestore db;
    String usuarioId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        FloatingActionButton fab = findViewById(R.id.fab);

        iniciarComponentes();

        fab.setOnClickListener(v -> {
            Intent intente = new Intent(ActMain.this, ActCadPedido.class);
            startActivity(intente);
            finish();
        });
        txtDeslogar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ActMain.this, ActLogin.class);
            startActivity(intent);
            finish();
        });

        //RecyclerView

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Carregando dados...");
        progressDialog.show();


        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        listaClientes = new ArrayList<>();
        myAdapter = new ClienteAdapter(ActMain.this, listaClientes);
        rv.setAdapter(myAdapter);

        EventChangeListener();

        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int id = (int) viewHolder.itemView.getTag();
                produtoRepositorio.excluir(id);
                List<Produto> dados = produtoRepositorio.buscarTodos();
                produtoAdapter = new ProdutoAdapter(this, dados);
                listDados.setAdapter(produtoAdapter);
            }
        });*/
    }

private void EventChangeListener() {

        db = FirebaseFirestore.getInstance();
        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ///Usuario/7yEjH5xoS5emCo8f88sN1x4j1wG3/Pedidos
        ///Usuario/7yEjH5xoS5emCo8f88sN1x4j1wG3/Clientes/HqOSQOwg7gMKUqhQMas1/Pedidos

        db.collection("Usuario/" + usuarioId + "/Clientes")
                .orderBy("nomeCliente")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value,
                                    @Nullable FirebaseFirestoreException error) {
                    if (error !=null){
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("Firestore erro!", "erro!"+error.getMessage());
                        return;
                    }
                    for (DocumentChange doc : value.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED){
                            listaClientes.add(doc.getDocument().toObject(Cliente.class));
                        }
                        myAdapter.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Usuario").document(usuarioId);
        documentReference.addSnapshotListener((documentSnapshot, error) -> {

            if (documentSnapshot != null){
                txtNome.setText(documentSnapshot.getString("nome"));
            }
        });
    }

    private void iniciarComponentes() {
        txtNome = findViewById(R.id.txtNomeUsuario);
        txtDeslogar = findViewById(R.id.txtDeslogar);
        rv = findViewById(R.id.rv_dados);
    }
}
