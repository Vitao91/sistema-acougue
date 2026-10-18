package com.acougue.service;

import com.acougue.dao.ProdutoDao;
import com.acougue.modelo.Produto;

import java.sql.SQLException;
import java.util.List;

/**
 * Camada de negócio para regras relacionadas a Produtos (cortes de carne).
 */
public class ProdutoService {

    private final ProdutoDao produtoDao;

    public ProdutoService() {
        this.produtoDao = new ProdutoDao();
    }

    public boolean cadastrar(Produto produto) throws SQLException {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto/corte não pode ser vazio.");
        }
        if (produto.getPrecoKg() <= 0) {
            throw new IllegalArgumentException("O preço por Kg deve ser maior que zero.");
        }
        if (produto.getEstoqueKg() < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }

        return produtoDao.cadastrar(produto);
    }

    public List<Produto> listarTodos() throws SQLException {
        return produtoDao.listarTodos();
    }

    public Produto buscarPorId(int id) throws SQLException {
        return produtoDao.buscarPorId(id);
    }

    public boolean excluir(int id) throws SQLException {
        return produtoDao.excluir(id);
    }
}
