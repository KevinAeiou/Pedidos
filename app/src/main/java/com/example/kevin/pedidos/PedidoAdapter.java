package com.example.kevin.pedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.meuViewHolder> {

    private ArrayList<Pedido> pedidos;
    private Context context;

    public PedidoAdapter(Context context, ArrayList<Pedido> listaPedidos) {
        this.pedidos = listaPedidos;
        this.context = context;
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

        holder.nomeCliente.setText(pedido.getNomeCliente());
        holder.nomeProduto.setText(pedido.getNomeProduto());
        holder.valor.setText(String.valueOf(pedido.getValor()));
        holder.quantidade.setText(String.valueOf(pedido.getQuantidade()));
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }
    public class meuViewHolder extends RecyclerView.ViewHolder{
        TextView nomeCliente;
        TextView nomeProduto;
        TextView valor;
        TextView quantidade;

        public meuViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeCliente = itemView.findViewById(R.id.txtItemNomeCli);
            nomeProduto = itemView.findViewById(R.id.txtItemNomeProd);
            valor = itemView.findViewById(R.id.txtItemValor);
            quantidade = itemView.findViewById(R.id.txtItemQuant);
        }
    }
}
