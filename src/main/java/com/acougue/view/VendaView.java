package com.acougue.view;

import com.acougue.modelo.Produto;
import com.acougue.modelo.Venda;
import com.acougue.service.ProdutoService;
import com.acougue.service.VendaService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Ponto de Venda (PDV) do Açougue.
 * Realiza o cálculo automático de (Peso em Kg × Preço por Kg),
 * abate o peso do estoque e registra a venda no banco de dados.
 */
public class VendaView extends JFrame {

    private final ProdutoService produtoService;
    private final VendaService vendaService;

    private JComboBox<Produto> cboProdutos;
    private JLabel lblPrecoKgValor;
    private JLabel lblEstoqueValor;
    private JTextField txtPesoKg;
    private JLabel lblTotalCalculado;
    private JButton btnFinalizarVenda;

    private JTable tblVendasRecentes;
    private DefaultTableModel modelVendas;
    private List<Produto> listaProdutos = new ArrayList<>();

    public VendaView() {
        this.produtoService = new ProdutoService();
        this.vendaService = new VendaService();

        initComponents();
        carregarProdutosCombo();
        carregarHistoricoVendas();
    }

    private void initComponents() {
        setTitle("Ponto de Venda (PDV) - Balança e Venda de Carnes");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Topo / Header
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnlHeader.setBackground(new Color(140, 25, 25));
        JLabel lblTitulo = new JLabel("Ponto de Venda & Balança Digital");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        pnlHeader.add(lblTitulo);
        add(pnlHeader, BorderLayout.NORTH);

        // Painel Principal Central
        JPanel pnlCenter = new JPanel(new BorderLayout(15, 15));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Seção Superior: Seleção de Carne e Cálculo
        JPanel pnlCalculadora = new JPanel(new GridLayout(1, 2, 20, 0));

        // Lado Esquerdo: Seleção do Produto e Balança
        JPanel pnlSelecao = new JPanel(new GridBagLayout());
        pnlSelecao.setBorder(BorderFactory.createTitledBorder("1. Selecione o Corte e o Peso"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Corte de Carne
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        pnlSelecao.add(new JLabel("Corte de Carne:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        cboProdutos = new JComboBox<>();
        cboProdutos.addActionListener(e -> atualizarDadosProdutoSelecionado());
        pnlSelecao.add(cboProdutos, gbc);

        // Preço por Kg
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        pnlSelecao.add(new JLabel("Preço / Kg:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        lblPrecoKgValor = new JLabel("R$ 0,00");
        lblPrecoKgValor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPrecoKgValor.setForeground(new Color(30, 100, 30));
        pnlSelecao.add(lblPrecoKgValor, gbc);

        // Estoque Atual
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        pnlSelecao.add(new JLabel("Estoque Atual:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        lblEstoqueValor = new JLabel("0,000 Kg");
        lblEstoqueValor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlSelecao.add(lblEstoqueValor, gbc);

        // Peso na Balança
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        pnlSelecao.add(new JLabel("Peso na Balança (Kg):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        txtPesoKg = new JTextField();
        txtPesoKg.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtPesoKg.setToolTipText("Digite o peso em quilos (ex: 1.250)");

        // Listener para cálculo automático em tempo real enquanto digita
        txtPesoKg.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { calcularTotalEmTempoReal(); }
            @Override
            public void removeUpdate(DocumentEvent e) { calcularTotalEmTempoReal(); }
            @Override
            public void changedUpdate(DocumentEvent e) { calcularTotalEmTempoReal(); }
        });
        pnlSelecao.add(txtPesoKg, gbc);

        pnlCalculadora.add(pnlSelecao);

        // Lado Direito: Painel de Display do Total e Botão de Vender
        JPanel pnlDisplayTotal = new JPanel(new BorderLayout(10, 10));
        pnlDisplayTotal.setBorder(BorderFactory.createTitledBorder("2. Valor da Venda"));

        JPanel pnlValorBox = new JPanel(new GridBagLayout());
        pnlValorBox.setBackground(new Color(245, 248, 245));
        pnlValorBox.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 180), 2));

        JLabel lblRotuloTotal = new JLabel("TOTAL A PAGAR");
        lblRotuloTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRotuloTotal.setForeground(new Color(80, 80, 80));

        lblTotalCalculado = new JLabel("R$ 0,00");
        lblTotalCalculado.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTotalCalculado.setForeground(new Color(34, 139, 34));

        GridBagConstraints gbcDisplay = new GridBagConstraints();
        gbcDisplay.gridx = 0; gbcDisplay.gridy = 0;
        pnlValorBox.add(lblRotuloTotal, gbcDisplay);
        gbcDisplay.gridy = 1;
        gbcDisplay.insets = new Insets(10, 0, 0, 0);
        pnlValorBox.add(lblTotalCalculado, gbcDisplay);

        pnlDisplayTotal.add(pnlValorBox, BorderLayout.CENTER);

        btnFinalizarVenda = new JButton("Confirmar Venda (Baixar Estoque)");
        btnFinalizarVenda.setBackground(new Color(34, 139, 34));
        btnFinalizarVenda.setForeground(Color.WHITE);
        btnFinalizarVenda.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnFinalizarVenda.setPreferredSize(new Dimension(200, 48));
        btnFinalizarVenda.addActionListener(e -> confirmarVenda());
        pnlDisplayTotal.add(btnFinalizarVenda, BorderLayout.SOUTH);

        pnlCalculadora.add(pnlDisplayTotal);
        pnlCenter.add(pnlCalculadora, BorderLayout.NORTH);

        // Seção Inferior: Histórico de Vendas Recentes
        String[] colunas = {"ID", "Corte", "Preço/Kg", "Peso (Kg)", "Total Pago", "Data e Hora"};
        modelVendas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblVendasRecentes = new JTable(modelVendas);
        tblVendasRecentes.setRowHeight(24);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        tblVendasRecentes.getColumnModel().getColumn(0).setCellRenderer(center);
        tblVendasRecentes.getColumnModel().getColumn(2).setCellRenderer(center);
        tblVendasRecentes.getColumnModel().getColumn(3).setCellRenderer(center);
        tblVendasRecentes.getColumnModel().getColumn(4).setCellRenderer(center);
        tblVendasRecentes.getColumnModel().getColumn(5).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(tblVendasRecentes);
        scroll.setBorder(BorderFactory.createTitledBorder("Vendas Recentes"));
        pnlCenter.add(scroll, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // Rodapé
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnRecarregar = new JButton("Atualizar Produtos");
        btnRecarregar.addActionListener(e -> carregarProdutosCombo());
        pnlFooter.add(btnRecarregar);

        JButton btnVoltar = new JButton("Fechar");
        btnVoltar.addActionListener(e -> dispose());
        pnlFooter.add(btnVoltar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private void carregarProdutosCombo() {
        try {
            listaProdutos = produtoService.listarTodos();
            cboProdutos.removeAllItems();
            for (Produto p : listaProdutos) {
                cboProdutos.addItem(p);
            }
            atualizarDadosProdutoSelecionado();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar carnes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarDadosProdutoSelecionado() {
        Produto selecionado = (Produto) cboProdutos.getSelectedItem();
        if (selecionado != null) {
            lblPrecoKgValor.setText(String.format("R$ %.2f", selecionado.getPrecoKg()));
            lblEstoqueValor.setText(String.format("%.3f Kg", selecionado.getEstoqueKg()));
            calcularTotalEmTempoReal();
        } else {
            lblPrecoKgValor.setText("R$ 0,00");
            lblEstoqueValor.setText("0,000 Kg");
            lblTotalCalculado.setText("R$ 0,00");
        }
    }

    private void calcularTotalEmTempoReal() {
        Produto selecionado = (Produto) cboProdutos.getSelectedItem();
        String pesoTexto = txtPesoKg.getText().trim().replace(",", ".");

        if (selecionado == null || pesoTexto.isEmpty()) {
            lblTotalCalculado.setText("R$ 0,00");
            return;
        }

        try {
            double peso = Double.parseDouble(pesoTexto);
            if (peso > 0) {
                double total = vendaService.calcularTotal(selecionado.getPrecoKg(), peso);
                lblTotalCalculado.setText(String.format("R$ %.2f", total));
            } else {
                lblTotalCalculado.setText("R$ 0,00");
            }
        } catch (NumberFormatException e) {
            lblTotalCalculado.setText("R$ 0,00");
        }
    }

    private void confirmarVenda() {
        Produto selecionado = (Produto) cboProdutos.getSelectedItem();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma carne para vender.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String pesoTexto = txtPesoKg.getText().trim().replace(",", ".");
        if (pesoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o peso da carne pesado na balança.", "Atenção", JOptionPane.WARNING_MESSAGE);
            txtPesoKg.requestFocus();
            return;
        }

        try {
            double peso = Double.parseDouble(pesoTexto);
            Venda vendaRealizada = vendaService.realizarVenda(selecionado.getId(), peso);

            String cupom = String.format(
                    "===================================\n" +
                    "           COMPROVANTE DE VENDA    \n" +
                    "===================================\n" +
                    "Corte: %s\n" +
                    "Preço / Kg: R$ %.2f\n" +
                    "Peso: %.3f Kg\n" +
                    "-----------------------------------\n" +
                    "TOTAL PAGO: R$ %.2f\n" +
                    "Data: %s\n" +
                    "===================================\n" +
                    "Venda registrada e estoque atualizado!",
                    vendaRealizada.getProdutoNome(),
                    vendaRealizada.getPrecoKg(),
                    vendaRealizada.getPesoKg(),
                    vendaRealizada.getValorTotal(),
                    vendaRealizada.getDataHoraFormatada()
            );

            JOptionPane.showMessageDialog(this, cupom, "Venda Realizada com Sucesso!", JOptionPane.INFORMATION_MESSAGE);

            // Limpa o campo de peso e recarrega dados atualizados
            txtPesoKg.setText("");
            lblTotalCalculado.setText("R$ 0,00");
            carregarProdutosCombo();
            carregarHistoricoVendas();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Peso inválido. Digite um número decimal (ex: 1.500).", "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar venda no banco: " + e.getMessage(), "Erro BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarHistoricoVendas() {
        modelVendas.setRowCount(0);
        try {
            List<Venda> vendas = vendaService.listarVendas();
            for (Venda v : vendas) {
                modelVendas.addRow(new Object[]{
                        v.getId(),
                        v.getProdutoNome(),
                        String.format("R$ %.2f", v.getPrecoKg()),
                        String.format("%.3f Kg", v.getPesoKg()),
                        String.format("R$ %.2f", v.getValorTotal()),
                        v.getDataHoraFormatada()
                });
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar histórico: " + e.getMessage());
        }
    }
}
