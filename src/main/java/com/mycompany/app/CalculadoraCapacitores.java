package com.mycompany.app;

public class CalculadoraCapacitores {

    public enum Mode { PARALLEL, SERIES }

    
    public static double requiredParallel(double cCurrent_uF, double cTarget_uF) {
        return cTarget_uF - cCurrent_uF;
    }

   
    public static double requiredSeries(double cCurrent_uF, double cTarget_uF) {
        if (cTarget_uF <= 0 || cCurrent_uF <= cTarget_uF) {
            return Double.NaN;
        }
        double c2 = (cTarget_uF * cCurrent_uF) / (cCurrent_uF - cTarget_uF);
        if (c2 <= 0) return Double.NaN;
        return c2;
    }

    public static String fmt(double value_uF) {
        if (Double.isNaN(value_uF)) return "No válido";
        if (Math.abs(value_uF) >= 1000.0) return String.format("%.3f mF", value_uF / 1000.0);
        if (Math.abs(value_uF) >= 1.0) return String.format("%.3f μF", value_uF);
        if (Math.abs(value_uF) >= 0.001) return String.format("%.3f nF", value_uF * 1000.0);
        return String.format("%.6f μF", value_uF);
    }
}
