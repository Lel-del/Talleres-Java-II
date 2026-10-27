package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public final class EntradaConsola {
    private EntradaConsola() { }
    public static Integer leerEntero(Scanner entrada) {
        while (entrada.hasNextLine()) {
            String linea = entrada.nextLine();
            // El Scanner auxiliar aisla cada linea; no deja saltos pendientes en el principal.
            Scanner numero = new Scanner(linea);
            try {
                if (!numero.hasNext()) {
                    System.err.println("Escribe un numero entero valido:"); continue;
                }
                int valor = numero.nextInt();
                if (numero.hasNext()) {
                    System.err.println("Escribe un solo numero entero valido:"); continue;
                }
                return valor;
            } catch (InputMismatchException e) {
                if (numero.hasNextLine()) numero.nextLine();
                System.err.println("Escribe un numero entero valido:");
            } finally {
                numero.close();
            }
        }
        return null;
    }
}
