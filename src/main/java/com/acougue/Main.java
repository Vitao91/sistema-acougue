package com.acougue;

import com.acougue.view.TelaPrincipal;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

/**
 * Ponto de entrada (Main) do Sistema de Açougue.
 */
public class Main {

    public static void main(String[] args) {
        // Configura o visual moderno (FlatLaf) para deixar a interface bonita e atual
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            // Fallback para o visual nativo do sistema operacional se o FlatLaf não estiver disponível
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }

        // Inicia a interface gráfica na thread segura do Swing
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}
