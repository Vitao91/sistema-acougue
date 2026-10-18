package com.acougue.dao;

import com.acougue.modelo.Venda;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para operações com a tabela de Vendas.
 */
public class VendaDao {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public boolean registrarVenda(Venda venda) throws SQLException {
        String sql = "INSERT INTO vendas (produto_id, produto_nome, preco_kg, peso_kg, valor_total, data_hora) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, venda.getProdutoId());
            stmt.setString(2, venda.getProdutoNome());
            stmt.setDouble(3, venda.getPrecoKg());
            stmt.setDouble(4, venda.getPesoKg());
            stmt.setDouble(5, venda.getValorTotal());
            stmt.setString(6, venda.getDataHora().format(FORMATTER));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        venda.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public List<Venda> listarTodas() throws SQLException {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT id, produto_id, produto_nome, preco_kg, peso_kg, valor_total, data_hora FROM vendas ORDER BY id DESC";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda v = new Venda();
                v.setId(rs.getInt("id"));
                v.setProdutoId(rs.getInt("produto_id"));
                v.setProdutoNome(rs.getString("produto_nome"));
                v.setPrecoKg(rs.getDouble("preco_kg"));
                v.setPesoKg(rs.getDouble("peso_kg"));
                v.setValorTotal(rs.getDouble("valor_total"));

                String dataStr = rs.getString("data_hora");
                if (dataStr != null && !dataStr.isEmpty()) {
                    try {
                        v.setDataHora(LocalDateTime.parse(dataStr, FORMATTER));
                    } catch (Exception e) {
                        v.setDataHora(LocalDateTime.now());
                    }
                }
                lista.add(v);
            }
        }
        return lista;
    }
}
