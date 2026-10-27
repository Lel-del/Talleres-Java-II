package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in)) {
            new AplicacionArenaRPG().ejecutar(entrada);
        }
    }
}
