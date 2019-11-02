package br.com.iagocolodetti.cadastrodeprodutossqlite;

import java.io.Serializable;

public class Produto implements Serializable {

    private static final long serialVersionUID = 3857089231473809967L;

    private int id;
    private String descricao, valor;

    public Produto(int id, String descricao, String valor) {
        super();
        setID(id);
        setDescricao(descricao);
        setValor(valor);
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
