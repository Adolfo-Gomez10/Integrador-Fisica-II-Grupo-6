package com.mycompany.app;

import java.util.Map;
import java.util.HashMap;

public class CalculadoraResistencias {
    private static final Map<String, Integer> DIGITS = new HashMap<>();
    private static final Map<String, Double> MULTIPLIERS = new HashMap<>();
    private static final Map<String, String> TOLERANCES = new HashMap<>();

    static {
        // Nombres de colores para dígitos (español, sin acentos)
        DIGITS.put("negro", 0);
        DIGITS.put("marron", 1);
        DIGITS.put("rojo", 2);
        DIGITS.put("naranja", 3);
        DIGITS.put("amarillo", 4);
        DIGITS.put("verde", 5);
        DIGITS.put("azul", 6);
        DIGITS.put("violeta", 7);
        DIGITS.put("gris", 8);
        DIGITS.put("blanco", 9);

        // Multiplicadores (español)
        MULTIPLIERS.put("negro", 1.0);
        MULTIPLIERS.put("marron", 10.0);
        MULTIPLIERS.put("rojo", 100.0);
        MULTIPLIERS.put("naranja", 1_000.0);
        MULTIPLIERS.put("amarillo", 10_000.0);
        MULTIPLIERS.put("verde", 100_000.0);
        MULTIPLIERS.put("azul", 1_000_000.0);
        MULTIPLIERS.put("oro", 0.1);
        MULTIPLIERS.put("plata", 0.01);

        // Tolerancias (español)
        TOLERANCES.put("marron", "±1%");
        TOLERANCES.put("rojo", "±2%");
        TOLERANCES.put("oro", "±5%");
        TOLERANCES.put("plata", "±10%");
        TOLERANCES.put("ninguno", "±20%");
    }

    public static String calculate4Band(String band1, String band2, String multiplier, String toleranceColor) {
        band1 = band1.toLowerCase();
        band2 = band2.toLowerCase();
        multiplier = multiplier.toLowerCase();
        toleranceColor = (toleranceColor == null || toleranceColor.isEmpty()) ? "ninguno" : toleranceColor.toLowerCase();

        if (!DIGITS.containsKey(band1) || !DIGITS.containsKey(band2) || !MULTIPLIERS.containsKey(multiplier)) {
            return "Colores inválidos";
        }

        int d1 = DIGITS.get(band1);
        int d2 = DIGITS.get(band2);
        double mult = MULTIPLIERS.get(multiplier);
        double value = ((d1 * 10) + d2) * mult;

        return formatOhms(value) + " " + TOLERANCES.getOrDefault(toleranceColor, "±?");
    }

    private static String formatOhms(double value) {
        if (value >= 1_000_000) return String.format("%.3f MΩ", value / 1_000_000.0);
        if (value >= 1_000) return String.format("%.3f kΩ", value / 1_000.0);
        return String.format("%.0f Ω", value);
    }
    
}
