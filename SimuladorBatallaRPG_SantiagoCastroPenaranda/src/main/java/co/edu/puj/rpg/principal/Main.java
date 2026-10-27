package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;
import java.util.Scanner;

/** Taller de encapsulamiento. Santiago Castro Peñaranda, M4. */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Personaje heroe = new Personaje("Santiago", 100, 20, 50.0);
        Enemigo enemigo = new Enemigo("Guardián de la arena", 150, 15, 120);
        System.out.printf("SIMULADOR DE BATALLA RPG - ARENA DE HÉROES%n");
        System.out.printf("Autor: Santiago Castro Peñaranda | Módulo: M4%n");
        System.out.printf("Estadísticas, precios y recompensas ficticios de demostración.%n");
        System.out.printf("Atacar, curarte o comprar con éxito permite un contraataque.%n");
        System.out.printf("Ver estado, volver y entradas inválidas no consumen turno.%n");
        ejecutarBatalla(scanner, heroe, enemigo);
    }

    // Sin modificador: accesible solo dentro de este paquete, no de sus subpaquetes.
    static void ejecutarBatalla(Scanner scanner, Personaje heroe, Enemigo enemigo) {
        StringBuilder historial = new StringBuilder();
        historial.append(String.format("Comienza el combate: %s contra %s.%n",
                heroe.getNombre(), enemigo.getNombre()));
        boolean huyendo = false;
        int ronda = 1;
        mostrarEstado(heroe, enemigo);

        do {
            System.out.printf("%n1. Atacar%n2. Usar poción / Tienda%n3. Ver estado%n4. Huir%n");
            int opcion = leerOpcion(scanner, 4, 4);
            boolean consumeTurno = false;
            switch (opcion) {
                case 1:
                    int vidaAnterior = enemigo.getPuntosVida();
                    heroe.atacar(enemigo);
                    historial.append(String.format("Ronda %d: %s ataca%s e inflige %d de daño.%n",
                            ronda, heroe.getNombre(), heroe.isUltimoAtaqueCritico()
                                    ? " con GOLPE CRÍTICO" : "",
                            vidaAnterior - enemigo.getPuntosVida()));
                    consumeTurno = true;
                    break;
                case 2:
                    consumeTurno = menuPociones(scanner, heroe, historial, ronda);
                    break;
                case 3:
                    mostrarEstado(heroe, enemigo);
                    break;
                case 4:
                    huyendo = true;
                    historial.append(String.format("%s abandona la arena.%n", heroe.getNombre()));
                    break;
                default:
                    System.out.printf("Opción inválida.%n");
            }

            if (consumeTurno && heroe.isEstaVivo() && enemigo.isEstaVivo()) {
                int vidaAnterior = heroe.getPuntosVida();
                enemigo.atacar(heroe);
                historial.append(String.format("%s contraataca: %d de daño real. Vida del héroe: %d/%d.%n",
                        enemigo.getNombre(), vidaAnterior - heroe.getPuntosVida(),
                        heroe.getPuntosVida(), heroe.getVidaMaxima()));
            }
            if (consumeTurno) {
                ronda++;
            }
            System.out.printf("%n=== HISTORIAL ACUMULADO ===%n%s", historial);
        } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyendo);

        if (huyendo) {
            historial.append(String.format("RESULTADO: HUIDA. Ambos conservan su estado actual.%n"));
        } else if (!heroe.isEstaVivo()) {
            historial.append(String.format("RESULTADO: DERROTA. %s ha caído.%n", heroe.getNombre()));
        } else {
            historial.append(String.format("RESULTADO: VICTORIA. %s ha sido derrotado.%n", enemigo.getNombre()));
            int nivelAnterior = heroe.getNivel();
            if (heroe.ganarExperiencia(enemigo)) {
                historial.append(String.format("Recompensa única: %d puntos de experiencia.%n",
                        enemigo.getExperienciaRecompensa()));
                if (heroe.getNivel() > nivelAnterior) {
                    historial.append(String.format("Subida de nivel: %d -> %d.%n",
                            nivelAnterior, heroe.getNivel()));
                }
            }
        }
        System.out.printf("%n=== HISTORIAL FINAL ===%n%s", historial);
        mostrarEstado(heroe, enemigo);
    }

    private static boolean menuPociones(Scanner scanner, Personaje heroe,
                                       StringBuilder historial, int ronda) {
        System.out.printf("%n=== POCIONES GRANDES ===%nInventario: %d | Oro: %.2f%n",
                heroe.getPocionesGrandes(), heroe.getOro());
        System.out.printf("Curación: %d | Precio de compra: %.2f%n",
                Personaje.CURACION_POCION_GRANDE, Personaje.PRECIO_POCION_GRANDE);
        System.out.printf("1. Usar una poción%n2. Comprar una poción%n3. Volver%n");
        int opcion = leerOpcion(scanner, 3, 3);
        switch (opcion) {
            case 1:
                int vidaAnterior = heroe.getPuntosVida();
                int inventarioAnterior = heroe.getPocionesGrandes();
                heroe.usarPocion(Personaje.CURACION_POCION_GRANDE);
                if (heroe.getPocionesGrandes() < inventarioAnterior) {
                    historial.append(String.format("Ronda %d: poción usada, curación real %d. Inventario: %d.%n",
                            ronda, heroe.getPuntosVida() - vidaAnterior, heroe.getPocionesGrandes()));
                    return true;
                }
                System.out.printf("No puedes usarla: vida completa, inventario vacío o personaje derrotado.%n");
                historial.append(String.format("Intento de curación sin efecto; inventario sin cambios.%n"));
                return false;
            case 2:
                double oroAnterior = heroe.getOro();
                if (heroe.comprarPocionGrande()) {
                    historial.append(String.format("Ronda %d: compra de poción. Oro: %.2f -> %.2f. Inventario: %d.%n",
                            ronda, oroAnterior, heroe.getOro(), heroe.getPocionesGrandes()));
                    return true;
                }
                System.out.printf("Compra rechazada: saldo insuficiente o personaje/inventario no disponible.%n");
                historial.append(String.format("Compra rechazada; oro e inventario sin cambios.%n"));
                return false;
            default:
                return false;
        }
    }

    private static int leerOpcion(Scanner scanner, int maximo, int opcionFinEntrada) {
        while (true) {
            System.out.printf("Elige una opción (1-%d): ", maximo);
            if (!scanner.hasNextLine()) {
                System.out.printf("%nFin de la entrada.%n");
                return opcionFinEntrada;
            }
            try {
                int opcion = Integer.parseInt(scanner.nextLine().trim());
                if (opcion >= 1 && opcion <= maximo) {
                    return opcion;
                } else {
                    System.out.printf("Opción fuera de rango.%n");
                }
            } catch (NumberFormatException e) {
                System.out.printf("Entrada inválida: escribe un número entero.%n");
            }
        }
    }

    private static void mostrarEstado(Personaje heroe, Enemigo enemigo) {
        System.out.printf("%n=== ESTADO DEL COMBATE ===%n");
        System.out.printf("Héroe: %s | Vida: %d/%d | Ataque base: %d | Vivo: %b%n",
                heroe.getNombre(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                heroe.getPuntosAtaque(), heroe.isEstaVivo());
        System.out.printf("Nivel: %d | Experiencia: %d/%d | Oro: %.2f | Pociones: %d%n",
                heroe.getNivel(), heroe.getExperiencia(), Personaje.EXPERIENCIA_POR_NIVEL,
                heroe.getOro(), heroe.getPocionesGrandes());
        System.out.printf("Enemigo: %s | Vida: %d/%d | Ataque: %d | Vivo: %b%n",
                enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima(),
                enemigo.getPuntosAtaque(), enemigo.isEstaVivo());
    }
}
