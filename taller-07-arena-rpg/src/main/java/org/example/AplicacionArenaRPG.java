package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class AplicacionArenaRPG {
    private final Arena arena = new Arena();
    // Registro general: conserva tambien a los personajes expulsados o caidos.
    private final List<Personaje> personajes = new ArrayList<>(arena.getHeroes());
    private final List<Gremio> gremios = new ArrayList<>();
    private boolean batallaIniciada;

    public void ejecutar(Scanner entrada) {
        int opcion;
        do {
            System.out.println("\n=== MENU ARENA DE HEROES RPG ===");
            System.out.println("1. Crear personaje\n2. Crear gremio\n3. Reclutar en escuadron");
            System.out.println("4. Equipar arma\n5. Mostrar estado detallado\n6. Salir");
            System.out.println("7. Unirse o cambiar de gremio\n8. Retirar del gremio");
            System.out.println("9. Desequipar arma\n10. Expulsar del escuadron\n11. Iniciar combate");
            opcion = numero(entrada, "Seleccione una opcion:", 1, 11);
            switch (opcion) {
                case 1: crearPersonaje(entrada); break;
                case 2: crearGremio(entrada); break;
                case 3:
                    if (bloqueada()) break;
                    Personaje recluta = seleccionarPersonaje(entrada);
                    System.out.println(arena.reclutar(recluta) ? "Personaje reclutado." :
                            "No se pudo reclutar: seleccion invalida, duplicado, caido o tablero lleno.");
                    break;
                case 4: equipar(entrada); break;
                case 5: mostrarReporte(); break;
                case 6: System.out.println("Cerrando la Arena de Heroes..."); break;
                case 7:
                    Personaje miembro = seleccionarPersonaje(entrada);
                    if (miembro == null) break;
                    Gremio gremio = seleccionarGremio(entrada);
                    if (gremio != null) {
                        miembro.unirseAGremio(gremio);
                        System.out.println("Gremio actualizado.");
                    }
                    break;
                case 8:
                    Personaje retirado = seleccionarPersonaje(entrada);
                    if (retirado != null) {
                        retirado.salirDeGremio();
                        System.out.println("Personaje sin gremio.");
                    }
                    break;
                case 9:
                    Personaje desarmado = seleccionarPersonaje(entrada);
                    if (desarmado != null) desarmado.desequiparArma();
                    break;
                case 10:
                    if (bloqueada()) break;
                    Personaje expulsado = seleccionarPersonaje(entrada);
                    if (expulsado != null) {
                        if (arena.getHeroes().contains(expulsado)) {
                            arena.getEscuadron().expulsarPersonaje(expulsado);
                            arena.actualizarTablero();
                            System.out.println("Expulsado; conserva sus atributos, arma y gremio.");
                        } else System.out.println("No pertenece al escuadron.");
                    }
                    break;
                case 11:
                    if (bloqueada()) break;
                    Personaje principal = personajes.get(0);
                    if (!arena.getHeroes().contains(principal) || !principal.estaVivo() || arena.hayDerrota()) {
                        System.out.println("Recluta a Thorin vivo antes de iniciar la batalla.");
                        break;
                    }
                    batallaIniciada = true;
                    arena.jugar(entrada);
                    System.out.println("Regreso a gestion: se conserva el resultado; reinicia el programa para otra batalla.");
                    break;
                default: break; // -1 indica que termino la entrada.
            }
        } while (opcion != 6 && opcion != -1 && entrada.hasNextLine());
    }

    private boolean bloqueada() {
        if (batallaIniciada) System.out.println("La batalla ya fue iniciada. Reinicia el programa para preparar otra.");
        return batallaIniciada;
    }

    private void crearPersonaje(Scanner entrada) {
        if (bloqueada()) return;
        String nombre = texto(entrada, "Nombre unico del personaje:");
        if (nombre == null) return;
        for (Personaje p : personajes) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Ya existe ese nombre."); return;
            }
        }
        int tipo = numero(entrada, "1. Guerrero  2. Mago", 1, 2);
        if (tipo == -1) return;
        int nivel = numero(entrada, "Nivel (1-20):", 1, 20);
        if (nivel == -1) return;
        Personaje nuevo = tipo == 1 ? new Guerrero(nombre, 120, nivel * 5, 0, 0, 10)
                : new Mago(nombre, 80, nivel * 5, 0, 0, 50);
        personajes.add(nuevo);
        System.out.println("Personaje creado libre. Reclutalo con la opcion 3 para combatir.");
    }

    private void crearGremio(Scanner entrada) {
        String nombre = texto(entrada, "Nombre del gremio:");
        if (nombre == null) return;
        for (Gremio g : gremios) {
            if (g.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Ya existe ese gremio."); return;
            }
        }
        gremios.add(new Gremio(nombre));
        System.out.println("Gremio creado.");
    }

    private void equipar(Scanner entrada) {
        Personaje p = seleccionarPersonaje(entrada);
        if (p == null) return;
        String nombre = texto(entrada, "Nombre del arma:");
        if (nombre == null) return;
        int dano = numero(entrada, "Dano del arma (0-1000):", 0, 1000);
        if (dano != -1) p.equiparArma(new Arma(nombre, dano));
    }

    private Personaje seleccionarPersonaje(Scanner entrada) {
        if (personajes.isEmpty()) { System.out.println("No hay personajes."); return null; }
        for (int i = 0; i < personajes.size(); i++)
            System.out.printf("%d. %s%n", i + 1, personajes.get(i).getNombre());
        int indice = numero(entrada, "Selecciona personaje:", 1, personajes.size());
        return indice == -1 ? null : personajes.get(indice - 1);
    }

    private Gremio seleccionarGremio(Scanner entrada) {
        if (gremios.isEmpty()) { System.out.println("No hay gremios. Crea uno primero."); return null; }
        for (int i = 0; i < gremios.size(); i++) System.out.printf("%d. %s%n", i + 1, gremios.get(i).getNombre());
        int indice = numero(entrada, "Selecciona gremio:", 1, gremios.size());
        return indice == -1 ? null : gremios.get(indice - 1);
    }

    private int numero(Scanner entrada, String mensaje, int minimo, int maximo) {
        System.out.println(mensaje);
        while (entrada.hasNextLine()) {
            try {
                int valor = Integer.parseInt(entrada.nextLine().trim());
                if (valor >= minimo && valor <= maximo) return valor;
            } catch (NumberFormatException e) {
                // Se muestra la misma indicacion para texto y numeros fuera de rango.
            }
            System.out.printf("Escribe un entero entre %d y %d:%n", minimo, maximo);
        }
        return -1;
    }

    private String texto(Scanner entrada, String mensaje) {
        System.out.println(mensaje);
        while (entrada.hasNextLine()) {
            String valor = entrada.nextLine().trim();
            if (!valor.isEmpty()) return valor;
            System.out.println("El nombre no puede estar vacio:");
        }
        return null;
    }

    public void mostrarReporte() {
        System.out.println(arena.getEscuadron());
        System.out.printf("%-20s %6s %8s %8s %8s %12s%n", "PERSONAJE", "NIVEL", "VIDA", "MANA", "ATAQUE", "ESCUADRON");
        for (Personaje p : personajes) {
            System.out.printf("%-20s %6d %8d %8d %8d %12s%n", p.getNombre(), p.getNivel(),
                    p.getVida(), p.getPuntosMana(), p.calcularAtaqueTotal(),
                    arena.getHeroes().contains(p) ? "Activo" : "Fuera");
            System.out.println(p);
        }
        for (Gremio g : gremios) System.out.println(g);
        System.out.printf("Pociones restantes: %d%n", arena.getInventario().getItems().size());
    }
}
