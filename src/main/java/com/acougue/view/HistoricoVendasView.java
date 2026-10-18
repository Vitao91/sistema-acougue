package com.acougue.view;

import com.acougue.modelo.Venda;
import com.acougue.service.VendaService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Tela de Relatório e Histórico Geral de Vendas.
 */
public class HistoricoVendasView extends JFrame {

    private final VendaService vendaService;
    private JTable tblVendas;
    private DefaultTableModel modelVendas;
    private JLabel lblTotalFaturado;
    private JLabel lblTotalKilos;
    private JLabel lblTotalAtendimentos;

    public HistoricoVendasView() {
        this.vendaService = new VendaService();
        initComponents();
        carregarVendas();
    }

    private void initComponents() {
        setTitle("Histórico e Faturamento de Vendas - Açougue");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Cabeçalho
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnlHeader.setBackground(new Color(60, 60, 70));
        JLabel lblTitulo = new JLabel("Histórico Geral de Vendas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        pnlHeader.add(lblTitulo);
        add(pnlHeader, BorderLayout.NORTH);

        // Painel de Indicadores (Cards de Resumo)
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 15, 0));
        pnlCards.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblTotalFaturado = criarCardIndicador(pnlCards, "Faturamento Total", "R$ 0,00", new Color(34, 139, 34));
        lblTotalKilos = criarCardIndicador(pnlCards, "Total Vendido (Kg)", "0,000 Kg", new Color(40, 90, 160));
        lblTotalAtendimentos = criarCardIndicador(pnlCards, "Qtd de Vendas", "0", new Color(180, 80, 30));

        add(pnlCards, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Corte / Carne", "Preço/Kg", "Peso (Kg)", "Valor Total (R$)", "Data e Hora"};
        modelVendas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblVendas = new JTable(modelVendas);
        tblVendas.setRowHeight(24);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblVendas.getColumnCount(); i++) {
            tblVendas.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(tblVendas);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlCards, BorderLayout.NORTH);
        pnlCenter.add(scroll, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // Rodapé
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton btnAtualizar = new JButton("Atualizar Dados");
        btnAtualizar.addActionListener(e -> carregarVendas());
        pnlFooter.add(btnAtualizar);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        pnlFooter.add(btnFechar);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    private JLabel criarCardIndicador(JPanel parent, String titulo, String valorInicial, Color corDestaque) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(Color.WHITE);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTit.setForeground(Color.GRAY);

        JLabel lblVal = new JLabel(valorInicial);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVal.setForeground(corDestaque);

        card.add(lblTit, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);
        parent.add(card);

        return lblVal;
    }

    private void carregarVendas() {
        modelVendas.setRowCount(0);
        double faturamento = 0;
        double kilos = 0;
        int totalVendas = 0;

        try {
            List<Venda> vendas = vendaService.listarVendas();
            for (Venda v : vendas) {
                faturamento += v.getValorTotal();
                kilos += v.getPesoKg();
                totalVendas++;

                modelVendas.addRow(new Object[]{
                        v.getId(),
                        v.getProdutoNome(),
                        String.format("R$ %.2f", v.getPrecoKg()),
                        String.format("%.3f Kg", v.getPesoKg()),
                        String.format("R$ %.2f", v.getValorTotal()),
                        v.getDataHoraFormatada()
                });
            }

            lblTotalFaturado.setText(String.format("R$ %.2f", faturamento));
            lblTotalKilos.setText(String.format("%.3f Kg", kilos));
            lblTotalAtendimentos.setText(String.valueOf(totalVendas));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vendas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
