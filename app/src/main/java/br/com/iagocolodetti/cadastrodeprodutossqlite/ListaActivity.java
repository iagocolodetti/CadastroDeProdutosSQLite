package br.com.iagocolodetti.cadastrodeprodutossqlite;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.ListActivity;
import android.content.Intent;

import java.util.ArrayList;

public class ListaActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ArrayList<Produto> lista = (ArrayList<Produto>) getIntent().getSerializableExtra("Produtos");

        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListaActivity.this, MainActivity.class);
                intent.putExtra("id", lista.get(position).getID());
                startActivity(intent);
            }
        });

        ProdutoArrayAdapter adapter = new ProdutoArrayAdapter(this, lista);

        setListAdapter(adapter);
    }

}