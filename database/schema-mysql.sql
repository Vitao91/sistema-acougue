-- ========================================================
-- Script de Criação do Banco de Dados para MySQL / MariaDB
-- Sistema de Gestão & PDV de Açougue
-- ========================================================

CREATE DATABASE IF NOT EXISTS acougue_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE acougue_db;

-- 1. Tabela de Produtos (Carnes e Mercadorias)
CREATE TABLE IF NOT EXISTS produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco_kg DECIMAL(10, 2) NOT NULL,
    estoque_kg DECIMAL(10, 3) NOT NULL
);

-- 2. Tabela de Vendas
CREATE TABLE IF NOT EXISTS vendas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    produto_id INT NOT NULL,
    produto_nome VARCHAR(100) NOT NULL,
    preco_kg DECIMAL(10, 2) NOT NULL,
    peso_kg DECIMAL(10, 3) NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    data_hora VARCHAR(30) NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE RESTRICT
);

-- Dados Iniciais para Demonstração
INSERT INTO produtos (nome, preco_kg, estoque_kg) VALUES
('Picanha Bovina', 69.90, 15.000),
('Contra Filé', 49.90, 20.000),
('Alcatra Especial', 44.90, 25.000),
('Costela Suína', 28.50, 18.000),
('Filé de Frango', 21.90, 30.000);
