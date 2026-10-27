package com.taller.poo;

import java.util.Scanner;

public class SistemaGestionCuentas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Arreglo fijo de exactamente tres cuentas. Todos son datos de demostración.
        CuentaBancaria[] cuentas = {
                new CuentaBancaria(),
                new CuentaBancaria("1001", "Santiago Castro Peñaranda"),
                new CuentaBancaria("1002", "Ana Ejemplo", 500.0, null)
        };
        System.out.printf("SISTEMA DE GESTIÓN DE CUENTAS%n");
        System.out.printf("Cuentas ficticias disponibles: 000000, 1001 y 1002.%n");
        System.out.printf("Cada ejecución comienza con los saldos de demostración.%n");
        int opcion;
        do {
            System.out.printf("%n1. Depositar en una cuenta%n2. Retirar de una cuenta%n");
            System.out.printf("3. Ver reporte consolidado%n4. Salir%n");
            opcion = leerOpcion(scanner);
            switch (opcion) {
                case 1:
                    procesarOperacion(scanner, cuentas, true);
                    break;
                case 2:
                    procesarOperacion(scanner, cuentas, false);
                    break;
                case 3:
                    mostrarReporte(cuentas);
                    break;
                case 4:
                    System.out.printf("Sistema finalizado. Hasta pronto.%n");
                    break;
                default:
                    System.out.printf("Opción no reconocida.%n");
            }
        } while (opcion != 4);
    }

    private static int leerOpcion(Scanner scanner) {
        while (true) {
            System.out.printf("Opción (1-4): ");
            if (!scanner.hasNextLine()) {
                return 4;
            }
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                if (opcion >= 1 && opcion <= 4) {
                    return opcion;
                } else {
                    System.out.printf("Opción fuera de rango. Usa 1, 2, 3 o 4.%n");
                }
            } catch (NumberFormatException e) {
                System.out.printf("Entrada inválida. Escribe un número entero.%n");
            }
        }
    }

    private static void procesarOperacion(Scanner scanner, CuentaBancaria[] cuentas,
                                          boolean esDeposito) {
        System.out.printf("Número de cuenta: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String numeroCuenta = scanner.nextLine().trim();
        CuentaBancaria encontrada = null;
        for (CuentaBancaria cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                encontrada = cuenta;
                break;
            }
        }
        if (encontrada == null) {
            System.out.printf("Error: no existe la cuenta '%s'.%n", numeroCuenta);
            return;
        }

        Double monto = leerMonto(scanner);
        if (monto == null) {
            return; // Final de la entrada, sin modificar la cuenta.
        }
        boolean exito = esDeposito ? encontrada.depositar(monto) : encontrada.retirar(monto);
        if (exito) {
            System.out.printf("Operación exitosa. Cuenta: %s | Saldo actual: %.2f%n",
                    encontrada.getNumeroCuenta(), encontrada.getSaldo());
        } else {
            System.out.printf("Operación rechazada: fondos insuficientes o resultado fuera de rango.%n");
        }
    }

    private static Double leerMonto(Scanner scanner) {
        while (true) {
            System.out.printf("Monto positivo (usa punto decimal, por ejemplo 25.50): ");
            if (!scanner.hasNextLine()) {
                return null;
            }
            try {
                double monto = Double.parseDouble(scanner.nextLine().trim());
                if (Double.isFinite(monto) && monto > 0.0) {
                    return monto;
                } else {
                    System.out.printf("El monto debe ser positivo y finito.%n");
                }
            } catch (NumberFormatException e) {
                System.out.printf("Monto inválido. Usa un número con punto decimal.%n");
            }
        }
    }

    private static void mostrarReporte(CuentaBancaria[] cuentas) {
        StringBuilder reporte = new StringBuilder();
        double saldoTotal = 0.0;
        reporte.append(String.format("%n=== REPORTE CONSOLIDADO ===%n"));
        for (CuentaBancaria cuenta : cuentas) {
            String credito = cuenta.getLimiteCredito() == null ? "No asignado (0 disponible)"
                    : ConversorMoneda.formatearMoneda(cuenta.getLimiteCredito());
            reporte.append(String.format("Cuenta: %s | Titular: %s | Saldo: %.2f | Crédito: %s%n",
                    cuenta.getNumeroCuenta(), cuenta.getTitular(), cuenta.getSaldo(), credito));
            saldoTotal += cuenta.getSaldo();
        }
        reporte.append(String.format("Cuentas registradas: %d%n", cuentas.length));
        reporte.append(String.format("Saldo total acumulado: %.2f%n", saldoTotal));
        System.out.printf("%s", reporte);
    }
}
