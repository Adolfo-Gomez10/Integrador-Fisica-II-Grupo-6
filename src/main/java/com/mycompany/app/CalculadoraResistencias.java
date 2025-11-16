package com.mycompany.app;

import java.util.Map;
import java.util.HashMap;

public class CalculadoraResistencias {
    private static final Map<String, Integer> DIGITS = new HashMap<>();
    private static final Map<String, Double> MULTIPLIERS = new HashMap<>();
    private static final Map<String, String> TOLERANCES = new HashMap<>();

    static {
        DIGITS.put("black", 0); DIGITS.put("brown", 1); DIGITS.put("red", 2);
        DIGITS.put("orange", 3); DIGITS.put("yellow", 4); DIGITS.put("green", 5);
        DIGITS.put("blue", 6); DIGITS.put("violet", 7); DIGITS.put("grey", 8);
        DIGITS.put("white", 9);

        MULTIPLIERS.put("black", 1.0); MULTIPLIERS.put("brown", 10.0); MULTIPLIERS.put("red", 100.0);
        MULTIPLIERS.put("orange", 1_000.0); MULTIPLIERS.put("yellow", 10_000.0);
        MULTIPLIERS.put("green", 100_000.0); MULTIPLIERS.put("blue", 1_000_000.0);
        MULTIPLIERS.put("gold", 0.1); MULTIPLIERS.put("silver", 0.01);

        TOLERANCES.put("brown", "±1%"); TOLERANCES.put("red", "±2%");
        TOLERANCES.put("gold", "±5%"); TOLERANCES.put("silver", "±10%");
        TOLERANCES.put("none", "±20%");
    }

    public static String calculate4Band(String band1, String band2, String multiplier, String toleranceColor) {
        band1 = band1.toLowerCase();
        band2 = band2.toLowerCase();
        multiplier = multiplier.toLowerCase();
        toleranceColor = (toleranceColor == null || toleranceColor.isEmpty()) ? "none" : toleranceColor.toLowerCase();

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
