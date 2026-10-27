package com.taller.poo;

/** Estudiante: Santiago Castro Peñaranda. Módulo: M4. */
public class Principal {
    public static void main(String[] args) {
        System.out.printf("Taller POO - Santiago Castro Peñaranda - M4%n");
        System.out.printf("Datos de demostración: cuentas, matrículas, calificaciones,%n");
        System.out.printf("vehículos y demás personas son ficticios. La tasa es de ejemplo.%n%n");

        Vehiculo[] vehiculos = {
                new Vehiculo(),
                new Vehiculo("Toyota", "Corolla", 15000.50, 180, true),
                new Vehiculo("Renault", "Logan", 8000.25, 170, false)
        };
        Estudiante[] estudiantes = {
                new Estudiante("EST-001", "Santiago Castro Peñaranda", 4.50, true),
                new Estudiante("EST-002", "Ana Ejemplo", 4.20, true),
                new Estudiante("EST-003", "Luis Ejemplo", 3.80, false)
        };
        // Un objeto por cada constructor sobrecargado.
        CuentaBancaria[] cuentas = {
                new CuentaBancaria(),
                new CuentaBancaria("1001", "Santiago Castro Peñaranda"),
                new CuentaBancaria("1002", "Ana Ejemplo", 500.0, null)
        };

        System.out.printf("=== OPERACIONES SOBRE CUENTAS ===%n");
        System.out.printf("Depósito 250.00 en 000000: %b%n", cuentas[0].depositar(250.0));
        System.out.printf("Retiro 50.00 en 000000: %b%n", cuentas[0].retirar(50.0));
        System.out.printf("Depósito 80.00 en 1001: %b%n", cuentas[1].depositar(80.0));
        System.out.printf("Retiro 150.00 en 1001 (usa crédito): %b%n", cuentas[1].retirar(150.0));
        System.out.printf("Retiro 40.00 en 1001 (excede disponible): %b%n", cuentas[1].retirar(40.0));
        System.out.printf("Depósito negativo en 1002: %b%n", cuentas[2].depositar(-10.0));
        System.out.printf("Retiro 100.00 en 1002 (crédito null): %b%n", cuentas[2].retirar(100.0));

        System.out.printf("%n=== REPORTE DE VEHÍCULOS ===%n");
        for (int i = 0; i < vehiculos.length; i++) {
            Vehiculo vehiculo = vehiculos[i];
            System.out.printf("[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                    i + 1, vehiculo.getMarca(), vehiculo.getModelo(), vehiculo.getKilometraje(),
                    vehiculo.getVelocidadMaxima(), vehiculo.isEncendido());
        }
        System.out.printf("%n=== REPORTE DE ESTUDIANTES ===%n");
        for (Estudiante estudiante : estudiantes) {
            System.out.printf("%s%n", estudiante.obtenerReporteAcademicos());
        }
        System.out.printf("%n=== REPORTE DE CUENTAS BANCARIAS ===%n");
        for (CuentaBancaria cuenta : cuentas) {
            String credito = cuenta.getLimiteCredito() == null ? "No asignado (0 disponible)"
                    : ConversorMoneda.formatearMoneda(cuenta.getLimiteCredito());
            System.out.printf("Cuenta: %s | Titular: %s | Saldo final: %s | Crédito: %s%n",
                    cuenta.getNumeroCuenta(), cuenta.getTitular(),
                    ConversorMoneda.formatearMoneda(cuenta.getSaldo()), credito);
        }

        double tasaEjemplo = 0.92;
        double euros = ConversorMoneda.convertirDolaresAEuros(100.0, tasaEjemplo);
        System.out.printf("%nConversión de ejemplo: 100.00 USD x %.2f = %.2f EUR%n", tasaEjemplo, euros);
        System.out.printf("Formato monetario de ejemplo: %s%n", ConversorMoneda.formatearMoneda(1250.75));
    }
}
