package com.tabacapp;

import com.tabacapp.db.DBConnection;  // clase que abre conexión, debes tenerla
import com.tabacapp.gui.MenuWindow;

import javax.swing.*;
import java.sql.Connection;

public class Ejecutar {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Crear conexión una vez
            Connection conn = DBConnection.getConnection();

            SwingUtilities.invokeLater(() -> new MenuWindow(conn));

        } catch (Exception e) {
            System.err.println("Error iniciando aplicación: " + e.getMessage());
        }
    }
}
