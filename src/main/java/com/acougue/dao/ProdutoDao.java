package com.acougue.dao;

import com.acougue.modelo.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para operações com a tabela de Produtos no banco.
 */
public class ProdutoDao {

    public boolean cadastrar(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, preco_kg, estoque_kg) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPrecoKg());
            stmt.setDouble(3, produto.getEstoqueKg());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        produto.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT id, nome, preco_kg, estoque_kg FROM produtos ORDER BY nome ASC";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setPrecoKg(rs.getDouble("preco_kg"));
                p.setEstoqueKg(rs.getDouble("estoque_kg"));
                lista.add(p);
            }
        }
        return lista;
    }

    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, preco_kg, estoque_kg FROM produtos WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto p = new Produto();
                    p.setId(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    p.setPrecoKg(rs.getDouble("preco_kg"));
                    p.setEstoqueKg(rs.getDouble("estoque_kg"));
                    return p;
                }
            }
        }
        return null;
    }

    public boolean atualizarEstoque(int id, double novoEstoque) throws SQLException {
        String sql = "UPDATE produtos SET estoque_kg = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, novoEstoque);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
