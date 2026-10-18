package com.acougue.view;

import javax.swing.*;
import java.awt.*;

/**
 * Tela Principal do Sistema de Açougue.
 * Centraliza o acesso às funções de Ponto de Venda (PDV),
 * Cadastro de Carnes e Relatórios.
 */
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gestão & PDV de Açougue");
        setSize(920, 600);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Barra de Menus Superior
        configurarMenuBar();

        // 2. Banner de Cabeçalho
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(160, 30, 30));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblNomeEmpresa = new JLabel("🥩 AÇOUGUE BOM CORTE");
        lblNomeEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblNomeEmpresa.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Sistema Integrado de Ponto de Venda (PDV) e Controle de Estoque");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(245, 220, 220));

        pnlHeader.add(lblNomeEmpresa, BorderLayout.NORTH);
        pnlHeader.add(lblSubtitulo, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        // 3. Painel Central com Botões Rápidos / Dashboard
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Card 1: Ponto de Venda (PDV)
        JButton btnPDV = criarBotaoCard(
                "🥩 Ponto de Venda (PDV)",
                "Pesar carnes na balança, calcular valor e registrar venda",
                new Color(34, 139, 34)
        );
        btnPDV.addActionListener(e -> abrirPDV());
        gbc.gridx = 0; gbc.gridy = 0;
        pnlCenter.add(btnPDV, gbc);

        // Card 2: Cadastro de Carnes e Estoque
        JButton btnCadastro = criarBotaoCard(
                "📦 Cadastro de Carnes & Estoque",
                "Cadastrar novos cortes, definir preços por Kg e gerenciar estoque",
                new Color(45, 100, 180)
        );
        btnCadastro.addActionListener(e -> abrirCadastro());
        gbc.gridx = 1; gbc.gridy = 0;
        pnlCenter.add(btnCadastro, gbc);

        // Card 3: Histórico de Vendas
        JButton btnHistorico = criarBotaoCard(
                "📊 Histórico & Faturamento",
                "Visualizar relatório de vendas realizadas e faturamento total",
                new Color(180, 85, 30)
        );
        btnHistorico.addActionListener(e -> abrirHistorico());
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        pnlCenter.add(btnHistorico, gbc);

        add(pnlCenter, BorderLayout.CENTER);

        // 4. Rodapé / Status Bar
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        pnlFooter.setBackground(new Color(240, 240, 240));

        JLabel lblStatusBD = new JLabel("● Banco de Dados: Conectado e Operacional");
        lblStatusBD.setForeground(new Color(0, 120, 0));
        lblStatusBD.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lblCreditos = new JLabel("Projeto Açougue | Desenvolvido por Victor");
        lblCreditos.setForeground(Color.GRAY);
        lblCreditos.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        pnlFooter.add(lblStatusBD, BorderLayout.WEST);
        pnlFooter.add(lblCreditos, BorderLayout.EAST);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void configurarMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Operações
        JMenu menuOperacoes = new JMenu("Operações");
        JMenuItem itemPDV = new JMenuItem("Ponto de Venda (PDV)");
        itemPDV.addActionListener(e -> abrirPDV());
        JMenuItem itemHistorico = new JMenuItem("Histórico de Vendas");
        itemHistorico.addActionListener(e -> abrirHistorico());
        menuOperacoes.add(itemPDV);
        menuOperacoes.add(itemHistorico);

        // Menu Cadastros
        JMenu menuCadastros = new JMenu("Cadastros");
        JMenuItem itemCarnes = new JMenuItem("Carnes e Mercadorias");
        itemCarnes.addActionListener(e -> abrirCadastro());
        menuCadastros.add(itemCarnes);

        // Menu Sistema
        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem itemSobre = new JMenuItem("Sobre o Projeto");
        itemSobre.addActionListener(e -> exibirSobre());
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        menuSistema.add(itemSobre);
        menuSistema.addSeparator();
        menuSistema.add(itemSair);

        menuBar.add(menuOperacoes);
        menuBar.add(menuCadastros);
        menuBar.add(menuSistema);

        setJMenuBar(menuBar);
    }

    private JButton criarBotaoCard(String titulo, String descricao, Color corTema) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(8, 8));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(corTema, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        btn.setBackground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTit.setForeground(corTema);

        JLabel lblDesc = new JLabel("<html><p style='color: #666666;'>" + descricao + "</p></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btn.add(lblTit, BorderLayout.NORTH);
        btn.add(lblDesc, BorderLayout.CENTER);

        return btn;
    }

    private void abrirPDV() {
        new VendaView().setVisible(true);
    }

    private void abrirCadastro() {
        new CadastrarProdutoView().setVisible(true);
    }

    private void abrirHistorico() {
        new HistoricoVendasView().setVisible(true);
    }

    private void exibirSobre() {
        String msg = "Sistema de Gestão & PDV de Açougue\n\n" +
                "Desenvolvido em Java Swing com Arquitetura em Camadas (MVC + DAO).\n" +
                "Funcionalidades:\n" +
                "- Cadastro e gerenciamento de carnes e estoque em Kg\n" +
                "- Ponto de Venda com cálculo de peso x preço\n" +
                "- Histórico de vendas e faturamento\n" +
                "- Conexão com banco de dados com suporte a SQLite e MySQL\n\n" +
                "Projeto de Victor.";
        JOptionPane.showMessageDialog(this, msg, "Sobre o Sistema", JOptionPane.INFORMATION_MESSAGE);
    }
}
