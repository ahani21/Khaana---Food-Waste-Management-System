package com.foodwaste;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.foodwaste.ui.LoginFrame;
import javax.swing.*;
import java.awt.*;

public class MainApp {
    public static void main(String[] args) {
        try {
            // Setup FlatLaf with the new Warm & Earthy Palette
            FlatMacLightLaf.setup();
            
            // GLOBAL DESIGN TOKENS
            UIManager.put("Panel.background", new Color(0xFAF6F1));
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            
            // Warm Brown Shadows (Tinted) - Emulated via Border colors in Swing
            UIManager.put("Component.focusedBorderColor", new Color(0x7BAE7F));
            UIManager.put("Button.focusedBorderColor", new Color(0x7BAE7F));
            
            // Table Styling
            UIManager.put("Table.alternateRowColor", new Color(0xFAF6F1));
            UIManager.put("Table.selectionBackground", new Color(0xFDF3E8));
            UIManager.put("Table.selectionForeground", new Color(0x3B2F2F));
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

            SwingUtilities.invokeLater(() -> {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
