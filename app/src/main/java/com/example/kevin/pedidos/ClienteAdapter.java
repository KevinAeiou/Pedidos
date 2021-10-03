package com.example.kevin.pedidos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.meuViewHolder> {

    private ArrayList<Cliente> clientes;
    private Context context;

    public ClienteAdapter (Context context, ArrayList<Cliente> listaClientes){
        this.clientes = listaClientes;
        this.context = context;

    }

    @NonNull
    @Override
    public ClienteAdapter.meuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_cliente, parent, false);
        return new meuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapter.meuViewHolder holder, int position) {

        Cliente cliente = clientes.get(position);

        holder.nomeCliente.setText(cliente.getNomeCliente());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActCadPedido.class);
                intent.putExtra("Cliente", clientes.get(position).getNomeCliente());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class meuViewHolder extends RecyclerView.ViewHolder {

        TextView nomeCliente;
        CardView cardView;

        public meuViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeCliente = itemView.findViewById(R.id.txtItemNomeCli);
            cardView = itemView.findViewById(R.id.cardViewId);
        }
    }
}
