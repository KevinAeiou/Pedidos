package com.example.kevin.pedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.meuViewHolder> {

    private ArrayList<Pedido> pedidos;
    private Context context;
    private Cliente cliente;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public PedidoAdapter(Context context, ArrayList<Pedido> listaPedidos) {
        this.pedidos = listaPedidos;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public PedidoAdapter.meuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);
        return new meuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewHolder holder, int position) {

        Pedido pedido = pedidos.get(position);

        holder.nomeProduto.setText(pedido.getNomeProduto());
        holder.valor.setText(String.valueOf(pedido.getValor()));
        holder.quantidade.setText(String.valueOf(pedido.getQuantidade()));

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario/"+auth.getCurrentUser().getUid()+"/Clientes/"+cliente.getClienteId()+"/Pedidos")
                        .document(pedidos.get(position).getPedidoId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    pedidos.remove(pedidos.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Pedido deletado", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Erro ao deletar" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }
    public class meuViewHolder extends RecyclerView.ViewHolder{
        TextView nomeProduto;
        TextView valor;
        TextView quantidade;
        ImageView deleteItem;

        public meuViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeProduto = itemView.findViewById(R.id.txtItemNomeProd);
            valor = itemView.findViewById(R.id.txtItemValor);
            quantidade = itemView.findViewById(R.id.txtItemQuant);
            deleteItem = itemView.findViewById(R.id.imgDelete);
        }
    }
}
