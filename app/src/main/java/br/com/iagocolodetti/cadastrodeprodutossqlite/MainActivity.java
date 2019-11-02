package br.com.iagocolodetti.cadastrodeprodutossqlite;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    SQLiteDatabase db;

    private final String NOME_BANCO = "ProdutoDB";
    private final String NOME_TABELA = "produto";

    private final String CHAVE_ID = "id";
    private final String CHAVE_DESCRICAO = "descricao";
    private final String CHAVE_QUANTIDADE = "quantidade";
    private final String CHAVE_PRECO = "preco";
    private final String CHAVE_DISPONIBILIDADE = "disponibilidade";

    private int idSelecionado = -1;

    TextView tvSelecionado;
    EditText etDescricao, etQuantidade, etPreco;
    Button btIncluir, btExcluir, btAlterar, btListar;
    RadioButton rSim, rNao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSelecionado = findViewById(R.id.tvSelecionado);

        etDescricao = findViewById(R.id.etDescricao);
        etQuantidade = findViewById(R.id.etQuantidade);
        etPreco = findViewById(R.id.etPreco);

        btIncluir = findViewById(R.id.btIncluir);
        btExcluir = findViewById(R.id.btExcluir);
        btAlterar = findViewById(R.id.btAlterar);
        btListar = findViewById(R.id.btListar);

        rSim = findViewById(R.id.rSim);
        rNao = findViewById(R.id.rNao);

        db = openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + NOME_TABELA + "(" +
                CHAVE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CHAVE_DESCRICAO + " TEXT," +
                CHAVE_QUANTIDADE + " INTEGER," +
                CHAVE_PRECO + " REAL," +
                CHAVE_DISPONIBILIDADE + " BINARY" + ");"
        );

        btIncluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idSelecionado != -1) {
                    limparCampos();
                    return;
                }
                if (etDescricao.getText().toString().trim().length() == 0 ||
                        etQuantidade.getText().toString().trim().length() == 0 ||
                        etPreco.getText().toString().trim().length() == 0) {
                    exibirMensagem("Erro: Todos os campos devem estar preenchidos.");
                    return;
                }
                if (!rSim.isChecked() && !rNao.isChecked()) {
                    exibirMensagem("Erro: Escolha a disponibilidade do produto.");
                    return;
                }

                db.execSQL("INSERT INTO " + NOME_TABELA + " VALUES" + "(" +
                        "NULL," +
                        "'" + etDescricao.getText().toString() + "'," +
                        "'" + Integer.parseInt(etQuantidade.getText().toString()) + "'," +
                        "'" + Double.parseDouble(etPreco.getText().toString()) + "'," +
                        "'" + (rSim.isChecked() ? 1 : 0) + "'" +");"
                );

                exibirMensagem("Produto incluído.");
                limparCampos();
            }
        });

        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idSelecionado == -1) {
                    exibirMensagem("Erro: Selecione um produto na lista.");
                    return;
                }

                Cursor c = db.rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " +
                        CHAVE_ID + "=" + idSelecionado + "", null);
                if (c.moveToFirst()) {
                    db.execSQL("DELETE FROM " + NOME_TABELA +
                            " WHERE " + CHAVE_ID + "=" + idSelecionado + "");
                    exibirMensagem("Produto excluído.");
                    limparCampos();
                } else {
                    exibirMensagem("Erro: Produto inexistente.");
                }
            }
        });

        btAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idSelecionado == -1) {
                    exibirMensagem("Erro: Selecione um produto na lista.");
                    return;
                }
                if (etDescricao.getText().toString().trim().length() == 0 ||
                        etQuantidade.getText().toString().trim().length() == 0 ||
                        etPreco.getText().toString().trim().length() == 0) {
                    exibirMensagem("Erro: Todos os campos devem estar preenchidos.");
                    return;
                }

                Cursor c = db.rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " +
                        CHAVE_ID + "=" + idSelecionado + "", null);
                if(c.moveToFirst()) {
                    db.execSQL("UPDATE " + NOME_TABELA + " SET " +
                            CHAVE_DESCRICAO + "='" + etDescricao.getText().toString() + "'," +
                            CHAVE_QUANTIDADE + "='" + Integer.parseInt(etQuantidade.getText().toString()) + "'," +
                            CHAVE_PRECO + "='" + Double.parseDouble(etPreco.getText().toString()) + "'," +
                            CHAVE_DISPONIBILIDADE + "='" + (rSim.isChecked() ? 1 : 0) + "'" +
                            " WHERE " + CHAVE_ID + "=" + idSelecionado + "");
                    exibirMensagem("Produto alterado.");
                    limparCampos();
                } else {
                    exibirMensagem("Erro: Produto inexistente.");
                }
            }
        });

        btListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = db.rawQuery("SELECT " +
                        CHAVE_ID + "," +
                        CHAVE_DESCRICAO + "," +
                        " CASE " + CHAVE_DISPONIBILIDADE +
                        " WHEN '1' THEN " +
                        CHAVE_QUANTIDADE +  " * " + CHAVE_PRECO +
                        " ELSE 'Valor Indisponível'" +
                        " END FROM " + NOME_TABELA, null);

                if (c.getCount() == 0) {
                    exibirMensagem("Erro: Não há produtos incluídos.");
                    return;
                }

                ArrayList<Produto> lista = new ArrayList<>();

                while (c.moveToNext()) {
                    lista.add(new Produto(c.getInt(0), c.getString(1), c.getString(2)));
                }

                Intent intent = new Intent(MainActivity.this, ListaActivity.class);
                intent.putExtra("Produtos", lista);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        idSelecionado = getIntent().getIntExtra("id", -1);
        if (idSelecionado != -1) {
            Cursor c = db.rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " +
                    CHAVE_ID + "='" + idSelecionado + "'", null);

            if (c.moveToFirst()) {
                tvSelecionado.setText("Produto \"" + c.getString(1) + "\" selecionado.");
                etDescricao.setText(c.getString(1));
                etQuantidade.setText(String.valueOf(c.getInt(2)));
                etPreco.setText(String.valueOf(c.getDouble(3)));
                if (c.getInt(4) == 1) {
                    rSim.setChecked(true);
                    rNao.setChecked(false);
                } else {
                    rSim.setChecked(false);
                    rNao.setChecked(true);
                }
                btIncluir.setText("Cancelar");
            } else {
                idSelecionado = -1;
                getIntent().removeExtra("id");
                exibirMensagem("Produto não encontrado.");
            }
        }
    }

    public void exibirMensagem(String mensagem) {
        Toast.makeText(this.getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    public void limparCampos() {
        getIntent().removeExtra("id");
        idSelecionado = -1;
        tvSelecionado.setText("");
        etDescricao.setText("");
        etQuantidade.setText("");
        etPreco.setText("");
        rSim.setChecked(true);
        rNao.setChecked(false);
        btIncluir.setText("Incluir");
        etDescricao.requestFocus();
    }

}
