package com.acougue.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gerenciador de conexão com o banco de dados.
 * Por padrão, utiliza SQLite local (arquivo 'acougue.db') para que o projeto
 * funcione imediatamente em qualquer computador sem precisar de instalação de servidor.
 * Também suporta conexão com MySQL caso configurado.
 */
public class Conexao {

    // Configuração para SQLite (automática, sem necessidade de usuário/senha)
    private static final String SQLITE_FILE = "acougue.db";
    private static final String SQLITE_URL = "jdbc:sqlite:" + SQLITE_FILE;

    // Configuração opcional para MySQL
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/acougue_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "";

    // Define qual banco usar: true para SQLite (recomendado para teste rápido), false para MySQL
    private static final boolean USAR_SQLITE = true;

    private static boolean inicializado = false;

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println(">>> Banco de dados inicializado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn;
        if (USAR_SQLITE) {
            conn = DriverManager.getConnection(SQLITE_URL);
        } else {
            conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        }

        if (!inicializado) {
            criarTabelasIniciais(conn);
            inicializado = true;
        }

        return conn;
    }

    /**
     * Cria automaticamente as tabelas necessárias se ainda não existirem,
     * garantindo que o projeto rode de primeira.
     */
    private static void criarTabelasIniciais(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Tabela de Produtos
            String sqlProdutos = USAR_SQLITE ?
                    "CREATE TABLE IF NOT EXISTS produtos (" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "  nome TEXT NOT NULL," +
                    "  preco_kg REAL NOT NULL," +
                    "  estoque_kg REAL NOT NULL" +
                    ");" :
                    "CREATE TABLE IF NOT EXISTS produtos (" +
                    "  id INT AUTO_INCREMENT PRIMARY KEY," +
                    "  nome VARCHAR(100) NOT NULL," +
                    "  preco_kg DECIMAL(10,2) NOT NULL," +
                    "  estoque_kg DECIMAL(10,3) NOT NULL" +
                    ");";
            stmt.execute(sqlProdutos);

            // Tabela de Vendas
            String sqlVendas = USAR_SQLITE ?
                    "CREATE TABLE IF NOT EXISTS vendas (" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "  produto_id INTEGER NOT NULL," +
                    "  produto_nome TEXT NOT NULL," +
                    "  preco_kg REAL NOT NULL," +
                    "  peso_kg REAL NOT NULL," +
                    "  valor_total REAL NOT NULL," +
                    "  data_hora TEXT NOT NULL" +
                    ");" :
                    "CREATE TABLE IF NOT EXISTS vendas (" +
                    "  id INT AUTO_INCREMENT PRIMARY KEY," +
                    "  produto_id INT NOT NULL," +
                    "  produto_nome VARCHAR(100) NOT NULL," +
                    "  preco_kg DECIMAL(10,2) NOT NULL," +
                    "  peso_kg DECIMAL(10,3) NOT NULL," +
                    "  valor_total DECIMAL(10,2) NOT NULL," +
                    "  data_hora VARCHAR(30) NOT NULL" +
                    ");";
            stmt.execute(sqlVendas);

            // Popula com dados de exemplo se a tabela estiver vazia
            var rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM produtos");
            if (rs.next() && rs.getInt("total") == 0) {
                stmt.execute("INSERT INTO produtos (nome, preco_kg, estoque_kg) VALUES ('Picanha Bovina', 69.90, 15.000);");
                stmt.execute("INSERT INTO produtos (nome, preco_kg, estoque_kg) VALUES ('Contra Filé', 49.90, 20.000);");
                stmt.execute("INSERT INTO produtos (nome, preco_kg, estoque_kg) VALUES ('Alcatra Especial', 44.90, 25.000);");
                stmt.execute("INSERT INTO produtos (nome, preco_kg, estoque_kg) VALUES ('Costela Suína', 28.50, 18.000);");
                stmt.execute("INSERT INTO produtos (nome, preco_kg, estoque_kg) VALUES ('Filé de Frango', 21.90, 30.000);");
            }
        } catch (SQLException e) {
            System.err.println("Aviso ao inicializar tabelas: " + e.getMessage());
        }
    }
}
