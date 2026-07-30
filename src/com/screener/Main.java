package com.screener;

import com.screener.ui.MainFrame;
import com.screener.util.UIConstants;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;

/**
 * Main Entry Point for the AI-Based Resume Screening and Candidate Ranking System.
 * 
 * Responsibilities:
 * 1. Sets up the application Look and Feel (L&F) and UI defaults for dark mode.
 * 2. Launches the MainFrame on the Swing Event Dispatch Thread (EDT) to ensure thread safety.
 */
public class Main {

    public static void main(String[] args) {
        // Set cross-platform look and feel and apply dark theme properties before initializing UI
        configureDarkThemeDefaults();

        // Swing components are not thread-safe. Always create and update UI on the EDT.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create and display the primary application frame
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                } catch (Exception e) {
                    System.err.println("Fatal Error initializing application UI: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sets global UIManager properties to enforce a modern dark theme across default Swing controls.
     */
    private static void configureDarkThemeDefaults() {
        try {
            // Use native cross-platform Look & Feel as a base
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            // Global Dark Color Overrides for Standard Swing Components
            UIManager.put("Panel.background", UIConstants.COLOR_BG_PRIMARY);
            UIManager.put("OptionPane.background", UIConstants.COLOR_BG_PRIMARY);
            UIManager.put("OptionPane.messageForeground", UIConstants.COLOR_TEXT_PRIMARY);
            
            UIManager.put("Table.background", UIConstants.COLOR_BG_CARD);
            UIManager.put("Table.foreground", UIConstants.COLOR_TEXT_PRIMARY);
            UIManager.put("Table.gridColor", UIConstants.COLOR_BORDER);
            UIManager.put("Table.selectionBackground", UIConstants.COLOR_ACCENT);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            
            UIManager.put("TableHeader.background", UIConstants.COLOR_BG_PRIMARY);
            UIManager.put("TableHeader.foreground", UIConstants.COLOR_TEXT_SECONDARY);
            
            UIManager.put("TextField.background", UIConstants.COLOR_BG_CARD);
            UIManager.put("TextField.foreground", UIConstants.COLOR_TEXT_PRIMARY);
            UIManager.put("TextField.caretForeground", UIConstants.COLOR_TEXT_PRIMARY);
            
            UIManager.put("ScrollPane.background", UIConstants.COLOR_BG_PRIMARY);
            UIManager.put("Viewport.background", UIConstants.COLOR_BG_PRIMARY);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Warning: Unable to set native dark Look and Feel. Defaulting to system style.");
        }
    }
}
