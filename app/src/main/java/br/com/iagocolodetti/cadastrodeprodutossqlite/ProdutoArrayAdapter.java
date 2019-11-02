package br.com.iagocolodetti.cadastrodeprodutossqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProdutoArrayAdapter extends ArrayAdapter<Produto> {

    private ArrayList<Produto> lista;

    public ProdutoArrayAdapter(Context context, ArrayList<Produto> lista) {
        super(context, R.layout.activity_lista, lista);
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View linha = LayoutInflater.from(getContext()).inflate(R.layout.activity_lista, null);

        TextView tvProduto = linha.findViewById(R.id.tvProduto);
        TextView tvValor = linha.findViewById(R.id.tvValor);

        Produto p = lista.get(position);

        tvProduto.setText(p.getDescricao());
        tvValor.setText(p.getValor());

        return linha;
    }

}
