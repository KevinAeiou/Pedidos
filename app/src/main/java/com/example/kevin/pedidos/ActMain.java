package com.example.kevin.pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActMain extends AppCompatActivity {

    private TextView txtNome, txtDeslogar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        FloatingActionButton fab = findViewById(R.id.fab);

        iniciarComponentes();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ActMain.this, ActCadPedido.class);
            startActivity(intent);
            finish();
        });
        txtDeslogar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ActMain.this, ActLogin.class);
            startActivity(intent);
            finish();
        });

        /*listDados.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        //RecyclerView
        listDados = findViewById(R.id.rv_dados);
        produtoAdapter = new ProdutoAdapter(this, dados);
        listDados.setLayoutManager(linearLayoutManager);
        listDados.setAdapter(produtoAdapter);

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
        }).attachToRecyclerView(listDados);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActMain.this, ActCadProduto.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
    }
}
