package com.acougue.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo que representa uma venda efetuada no açougue.
 */
public class Venda {

    private int id;
    private int produtoId;
    private String produtoNome;
    private double precoKg;
    private double pesoKg;
    private double valorTotal;
    private LocalDateTime dataHora;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Venda() {
        this.dataHora = LocalDateTime.now();
    }

    public Venda(int produtoId, String produtoNome, double precoKg, double pesoKg, double valorTotal) {
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.precoKg = precoKg;
        this.pesoKg = pesoKg;
        this.valorTotal = valorTotal;
        this.dataHora = LocalDateTime.now();
    }

    public Venda(int id, int produtoId, String produtoNome, double precoKg, double pesoKg, double valorTotal, LocalDateTime dataHora) {
        this.id = id;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.precoKg = precoKg;
        this.pesoKg = pesoKg;
        this.valorTotal = valorTotal;
        this.dataHora = dataHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public double getPrecoKg() {
        return precoKg;
    }

    public void setPrecoKg(double precoKg) {
        this.precoKg = precoKg;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getDataHoraFormatada() {
        if (dataHora == null) return "";
        return dataHora.format(FORMATTER);
    }
}
