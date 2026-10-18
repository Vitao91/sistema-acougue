package com.acougue.modelo;

/**
 * Modelo que representa um produto (corte de carne / mercadoria) no açougue.
 */
public class Produto {

    private int id;
    private String nome;
    private double precoKg;
    private double estoqueKg;

    public Produto() {
    }

    public Produto(int id, String nome, double precoKg, double estoqueKg) {
        this.id = id;
        this.nome = nome;
        this.precoKg = precoKg;
        this.estoqueKg = estoqueKg;
    }

    public Produto(String nome, double precoKg, double estoqueKg) {
        this.nome = nome;
        this.precoKg = precoKg;
        this.estoqueKg = estoqueKg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoKg() {
        return precoKg;
    }

    public void setPrecoKg(double precoKg) {
        this.precoKg = precoKg;
    }

    public double getEstoqueKg() {
        return estoqueKg;
    }

    public void setEstoqueKg(double estoqueKg) {
        this.estoqueKg = estoqueKg;
    }

    @Override
    public String toString() {
        return String.format("%s (R$ %.2f / Kg)", nome, precoKg);
    }
}
