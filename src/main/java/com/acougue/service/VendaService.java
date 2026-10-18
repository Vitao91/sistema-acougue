package com.acougue.service;

import com.acougue.dao.ProdutoDao;
import com.acougue.dao.VendaDao;
import com.acougue.modelo.Produto;
import com.acougue.modelo.Venda;

import java.sql.SQLException;
import java.util.List;

/**
 * Camada de negócio responsável pelo Ponto de Venda (PDV),
 * cálculo do valor por peso (Kg) e baixa de estoque.
 */
public class VendaService {

    private final ProdutoDao produtoDao;
    private final VendaDao vendaDao;

    public VendaService() {
        this.produtoDao = new ProdutoDao();
        this.vendaDao = new VendaDao();
    }

    /**
     * Calcula o valor total com base no preço por Kg e no peso pesado na balança.
     */
    public double calcularTotal(double precoKg, double pesoKg) {
        if (precoKg < 0 || pesoKg < 0) {
            return 0.0;
        }
        return Math.round((precoKg * pesoKg) * 100.0) / 100.0;
    }

    /**
     * Executa a venda: valida estoque, calcula valor final,
     * abate o peso vendido do estoque e registra a venda.
     */
    public Venda realizarVenda(int produtoId, double pesoKg) throws SQLException {
        if (pesoKg <= 0) {
            throw new IllegalArgumentException("O peso informado deve ser maior que 0 Kg.");
        }

        Produto produto = produtoDao.buscarPorId(produtoId);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado no sistema (Código: " + produtoId + ").");
        }

        if (produto.getEstoqueKg() < pesoKg) {
            throw new IllegalStateException(String.format(
                    "Estoque insuficiente! Disponível: %.3f Kg | Solicitado: %.3f Kg",
                    produto.getEstoqueKg(), pesoKg
            ));
        }

        double valorTotal = calcularTotal(produto.getPrecoKg(), pesoKg);

        // Atualiza o estoque no banco
        double novoEstoque = produto.getEstoqueKg() - pesoKg;
        produtoDao.atualizarEstoque(produtoId, novoEstoque);

        // Registra a venda
        Venda venda = new Venda(produto.getId(), produto.getNome(), produto.getPrecoKg(), pesoKg, valorTotal);
        vendaDao.registrarVenda(venda);

        return venda;
    }

    public List<Venda> listarVendas() throws SQLException {
        return vendaDao.listarTodas();
    }
}
