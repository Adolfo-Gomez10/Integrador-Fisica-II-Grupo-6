package com.mycompany.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InterfazGrafica {
    private static final String[] COLORS = {
        "negro","marron","rojo","naranja","amarillo","verde","azul","violeta","gris","blanco","oro","plata","ninguno"
    };

    public static void createAndShow() {
        JFrame frame = new JFrame("Calculadoras - Resistencias y Capacitores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 350);
        JTabbedPane tabs = new JTabbedPane();

        // Resistor interfaz
        JPanel rpanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JComboBox<String> b1 = new JComboBox<>(COLORS);
        JComboBox<String> b2 = new JComboBox<>(COLORS);
        JComboBox<String> mult = new JComboBox<>(new String[]{"negro","marron","rojo","naranja","amarillo","verde","azul","violeta","gris","blanco","oro","plata"});
        JComboBox<String> tol = new JComboBox<>(new String[]{"marron","rojo","oro","plata","ninguno"});
        JButton rcalc = new JButton("Calcular Resistencia");
        JLabel rres = new JLabel("Valor: ");

        c.insets = new Insets(5,5,5,5);
        c.gridx=0; c.gridy=0; rpanel.add(new JLabel("Banda 1:"), c);
        c.gridx=1; rpanel.add(b1, c);
        c.gridx=0; c.gridy=1; rpanel.add(new JLabel("Banda 2:"), c);
        c.gridx=1; rpanel.add(b2, c);
        c.gridx=0; c.gridy=2; rpanel.add(new JLabel("Multiplicador:"), c);
        c.gridx=1; rpanel.add(mult, c);
        c.gridx=0; c.gridy=3; rpanel.add(new JLabel("Tolerancia:"), c);
        c.gridx=1; rpanel.add(tol, c);
        c.gridx=0; c.gridy=4; c.gridwidth=2; rpanel.add(rcalc, c);
        c.gridy=5; rpanel.add(rres, c);

        rcalc.addActionListener((ActionEvent e) -> {
            String val = CalculadoraResistencias.calculate4Band((String)b1.getSelectedItem(), (String)b2.getSelectedItem(),
                    (String)mult.getSelectedItem(), (String)tol.getSelectedItem());
            rres.setText("Valor: " + val);
        });

        // Capacitor interfaz
        JPanel cpanel = new JPanel(new GridBagLayout());
        JLabel curL = new JLabel("Valor actual (μF):");
        JTextField curF = new JTextField("1.0", 10);
        JLabel tgtL = new JLabel("Valor objetivo (μF):");
        JTextField tgtF = new JTextField("2.0", 10);
        JComboBox<String> mode = new JComboBox<>(new String[]{"PARALELO","SERIE"});
        JButton ccalc = new JButton("Calcular capacitor a agregar");
        JLabel cres = new JLabel("Resultado: ");

        c.gridx=0; c.gridy=0; c.gridwidth=1; cpanel.add(curL, c);
        c.gridx=1; cpanel.add(curF, c);
        c.gridx=0; c.gridy=1; cpanel.add(tgtL, c);
        c.gridx=1; cpanel.add(tgtF, c);
        c.gridx=0; c.gridy=2; cpanel.add(new JLabel("Modo:"), c);
        c.gridx=1; cpanel.add(mode, c);
        c.gridx=0; c.gridy=3; c.gridwidth=2; cpanel.add(ccalc, c);
        c.gridy=4; cpanel.add(cres, c);

        ccalc.addActionListener((ActionEvent e) -> {
            try {
                double cur = Double.parseDouble(curF.getText());
                double tgt = Double.parseDouble(tgtF.getText());
                String m = (String)mode.getSelectedItem();
                double required;
                if ("PARALELO".equalsIgnoreCase(m)) required = CalculadoraCapacitores.requiredParallel(cur, tgt);
                else required = CalculadoraCapacitores.requiredSeries(cur, tgt);
                String out = (Double.isNaN(required) ? "No válido (verifique valores y modo)" : "Agregar: " + CalculadoraCapacitores.fmt(required));
                cres.setText("Resultado: " + out);
            } catch (NumberFormatException ex) {
                cres.setText("Resultado: Entrada numérica inválida");
            }
        });

        tabs.add("Resistencia (colores)", rpanel);
        tabs.add("Capacitor (calcular a agregar)", cpanel);
        frame.getContentPane().add(tabs);
        frame.setVisible(true);
    }
}