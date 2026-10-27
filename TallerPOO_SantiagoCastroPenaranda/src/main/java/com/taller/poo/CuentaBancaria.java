package com.taller.poo;

public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private Double limiteCredito;

    /*
     * ANÁLISIS
     * 1. Un atributo double contiene un valor primitivo y su valor inicial por
     *    defecto es 0.0. Double es una referencia a un objeto; su valor por
     *    defecto es null. El objeto Wrapper y su referencia pueden implicar
     *    memoria adicional, cuyo tamaño depende de la JVM. Double admite null.
     * 2. En una operación aritmética Java desempaqueta Double invocando su valor
     *    primitivo. Si la referencia es null, ese acceso causa
     *    NullPointerException. Un atributo double ya tiene un valor numérico.
     *    Las variables LOCALES, por su parte, deben inicializarse antes de usarse:
     *    no reciben automáticamente estos valores por defecto.
     */
    public CuentaBancaria() {
        this("000000", "Sin Nombre", 0.0, 0.0);
    }

    public CuentaBancaria(String numeroCuenta, String titular) {
        this(numeroCuenta, titular, 0.0, 100.0);
    }

    public CuentaBancaria(String numeroCuenta, String titular,
                          double saldo, Double limiteCredito) {
        if (!Double.isFinite(saldo)
                || (limiteCredito != null
                    && (!Double.isFinite(limiteCredito) || limiteCredito < 0.0))) {
            throw new IllegalArgumentException("Saldo finito y crédito no negativo requeridos.");
        }
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.limiteCredito = limiteCredito;
    }

    public boolean depositar(double monto) {
        if (Double.isFinite(monto) && monto > 0.0
                && Double.isFinite(this.saldo + monto)) {
            this.saldo += monto;
            return true;
        } else {
            return false;
        }
    }

    public boolean retirar(double monto) {
        // Primero se comprueba null; así evitamos desempaquetarlo por accidente.
        double creditoDisponible = this.limiteCredito == null ? 0.0 : this.limiteCredito;
        double nuevoSaldo = this.saldo - monto;
        if (Double.isFinite(monto) && monto > 0.0
                && Double.isFinite(nuevoSaldo)
                && nuevoSaldo >= -creditoDisponible) {
            // Equivale a monto <= saldo + crédito, evitando desbordar esa suma.
            this.saldo = nuevoSaldo;
            return true;
        } else {
            return false;
        }
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTitular() { return titular; }
    public double getSaldo() { return saldo; }
    public Double getLimiteCredito() { return limiteCredito; }
}
