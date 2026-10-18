package com.acougue.view;

import com.acougue.modelo.Produto;
import com.acougue.service.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela de Cadastro e Gerenciamento de Produtos (Carnes e Mercadorias).
 */
public class CadastrarProdutoView extends JFrame {

    private final ProdutoService produtoService;

    private JTextField txtNome;
    private JTextField txtPrecoKg;
    private JTextField txtEstoqueKg;
    private JTable tblProdutos;
    private DefaultTableModel tableModel;

    public CadastrarProdutoView() {
        this.produtoService = new ProdutoService();
        initComponents();
        carregarProdutosNaTabela();
    }

    private void initComponents() {
        setTitle("Cadastro e Gestão de Carnes - Açougue");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Cabeçalho
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnlHeader.setBackground(new Color(180, 40, 40));
        JLabel lblTitulo = new JLabel("Cadastro e Estoque de Carnes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        pnlHeader.add(lblTitulo);
        add(pnlHeader, BorderLayout.NORTH);

        // Painel Central com Formulário e Tabela
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Formulário de Cadastro
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Novo Corte / Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        pnlForm.add(new JLabel("Nome da Mercadoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtNome = new JTextField(20);
        pnlForm.add(txtNome, gbc);

        // Linha 1: Preço por Kg
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        pnlForm.add(new JLabel("Preço por Kg (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtPrecoKg = new JTextField(20);
        txtPrecoKg.setToolTipText("Exemplo: 49.90");
        pnlForm.add(txtPrecoKg, gbc);

        // Linha 2: Estoque em Kg
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        pnlForm.add(new JLabel("Estoque Inicial (Kg):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtEstoqueKg = new JTextField(20);
        txtEstoqueKg.setToolTipText("Exemplo: 20.500");
        pnlForm.add(txtEstoqueKg, gbc);

        // Botões do formulário
        JPanel pnlBotoesForm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        pnlBotoesForm.add(btnLimpar);

        JButton btnSalvar = new JButton("Salvar Carne");
        btnSalvar.setBackground(new Color(40, 140, 60));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarProduto());
        pnlBotoesForm.add(btnSalvar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        pnlForm.add(pnlBotoesForm, gbc);

        pnlCenter.add(pnlForm, BorderLayout.NORTH);

        // Tabela de Produtos cadastrados
        String[] colunas = {"Código", "Mercadoria", "Preço / Kg (R$)", "Estoque Disponível (Kg)"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblProdutos = new JTable(tableModel);
        tblProdutos.setRowHeight(24);
        tblProdutos.getTableHeader().setReorderingAllowed(false);

        // Centralizar colunas numéricas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblProdutos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblProdutos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblProdutos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tblProdutos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados no Banco"));
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // Rodapé com Ações da Tabela
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnExcluir = new JButton("Excluir Selecionado");
        btnExcluir.addActionListener(e -> excluirProduto());
        pnlFooter.add(btnExcluir);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        pnlFooter.add(btnVoltar);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void salvarProduto() {
        String nome = txtNome.getText().trim();
        String precoStr = txtPrecoKg.getText().trim().replace(",", ".");
        String estoqueStr = txtEstoqueKg.getText().trim().replace(",", ".");

        if (nome.isEmpty() || precoStr.isEmpty() || estoqueStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double preco = Double.parseDouble(precoStr);
            double estoque = Double.parseDouble(estoqueStr);

            Produto p = new Produto(nome, preco, estoque);
            boolean sucesso = produtoService.cadastrar(p);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Produto '" + nome + "' cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarProdutosNaTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível cadastrar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço ou Estoque inválido. Digite apenas valores numéricos.", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro no banco de dados: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        int selectedRow = tblProdutos.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int codigo = (int) tableModel.getValueAt(selectedRow, 0);
        String nome = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir o produto '" + nome + "' (Código: " + codigo + ")?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (produtoService.excluir(codigo)) {
                    JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarProdutosNaTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível excluir o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void carregarProdutosNaTabela() {
        tableModel.setRowCount(0);
        try {
            List<Produto> lista = produtoService.listarTodos();
            for (Produto p : lista) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        String.format("R$ %.2f", p.getPrecoKg()),
                        String.format("%.3f Kg", p.getEstoqueKg())
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtPrecoKg.setText("");
        txtEstoqueKg.setText("");
        txtNome.requestFocus();
    }
}
