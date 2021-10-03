package com.example.kevin.pedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
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

    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class meuViewHolder extends RecyclerView.ViewHolder {

        TextView nomeCliente;

        public meuViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeCliente = itemView.findViewById(R.id.txtItemNomeCli);
        }
    }
}
