package com.taller.poo;

public final class ConversorMoneda {
    private ConversorMoneda() {}

    public static double convertirDolaresAEuros(double montoDolares, double tasaCambio) {
        if (!Double.isFinite(montoDolares) || !Double.isFinite(tasaCambio)
                || montoDolares <= 0.0 || tasaCambio <= 0.0) {
            return 0.0;
        }
        double resultado = montoDolares * tasaCambio;
        return Double.isFinite(resultado) ? resultado : 0.0;
    }

    public static String formatearMoneda(double monto) {
        return String.format("$%.2f", monto);
    }
}
