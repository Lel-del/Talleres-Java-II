package org.example;

import org.example.model.enums.TipoPersonaje;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.example.engine.Bestiario;
import org.example.engine.Estadisticas;
import org.example.exception.CapacidadExcedidaException;
import org.example.exception.SinManaException;

public final class Arena {
    private final PerfilJugador perfil = new PerfilJugador();
    private final Bestiario bestiario = new Bestiario(perfil);
    private final Estadisticas estadisticas = new Estadisticas();
    private final Escuadron escuadron = new Escuadron("Heroes de la Arena");
    // Vista de la misma lista: no es una segunda coleccion de heroes.
    private final List<Personaje> heroes = escuadron.getMiembros();
    private final List<Personaje> enemigos = new ArrayList<>();
    private final Inventario<Pocion> inventario = new Inventario<>();
    private final char[][] tablero = new char[5][5];
    private final Guerrero principal = new Guerrero("Thorin", 120, 15, 0, 0, 10, TipoPersonaje.GUERRERO);
    private static final int COFRE_X = 2;
    private static final int COFRE_Y = 2;

    public Arena() {
        escuadron.reclutarPersonaje(principal);
        escuadron.reclutarPersonaje(new Mago("Gandalf", 80, 25, 1, 1, 50, TipoPersonaje.MAGO));
        enemigos.add(new Guerrero("Orco", 60, 16, 4, 0, 3, TipoPersonaje.GUERRERO));
        enemigos.add(new Mago("Brujo", 45, 12, 0, 4, 20, TipoPersonaje.MAGO));
        enemigos.add(new Guerrero("Guardian", 70, 18, 4, 4, 5, TipoPersonaje.GUERRERO));
        try {
            inventario.agregar(new Pocion(35));
            inventario.agregar(new Pocion(35));
            inventario.agregar(new Pocion(50));
        } catch (CapacidadExcedidaException e) {
            throw new IllegalStateException("Configuracion inicial del inventario incorrecta.", e);
        }
        actualizarTablero();
    }

    public void jugar(Scanner entrada) {
        if (hayVictoria()) {
            System.out.println("La arena ya fue conquistada.");
            return;
        }
        if (!heroes.contains(principal) || hayDerrota()) {
            System.out.println("No se puede combatir: recluta a Thorin vivo y conserva heroes activos.");
            return;
        }
        System.out.println("ARENA RPG - Derrota a todos los enemigos y lleva a Thorin al cofre (2,2).");
        System.out.println("X = columna; Y = fila. Ataques sin limite de distancia.");
        int ronda = 1;
        do {
            System.out.println("\nRONDA " + ronda++);
            mostrarEstado();
            for (Personaje heroe : heroes) {
                if (!heroe.actualizarTurno()) {
                    actualizarTablero();
                    if (hayDerrota()) break;
                    continue;
                }
                if (!turnoHeroe(heroe, entrada)) {
                    System.out.println("Entrada finalizada. Partida interrumpida.");
                    return;
                }
                if (hayVictoria() || hayDerrota()) break;
            }
            if (!hayVictoria() && !hayDerrota()) turnoEnemigos();
            limpiarBajas();
        } while (!hayVictoria() && !hayDerrota());
        mostrarEstado();
        if (hayVictoria()) {
            perfil.registrarLogro("Arena conquistada");
            estadisticas.incrementar("Victorias de arena");
        }
        System.out.println(hayVictoria() ? "VICTORIA: enemigos eliminados y cofre alcanzado." :
                "DERROTA: el escuadron o su heroe principal ha caido.");
    }

    private boolean turnoHeroe(Personaje heroe, Scanner entrada) {
        while (true) {
            System.out.println("\nTurno de " + heroe.getEstado());
            System.out.println("1. Atacar  2. Usar pocion  3. Mover");
            Integer opcion = leerEntero(entrada);
            if (opcion == null) return false;
            switch (opcion) {
                case 1:
                    List<Personaje> objetivos = new ArrayList<>();
                    for (Personaje enemigo : enemigos) {
                        if (enemigo.estaVivo()) objetivos.add(enemigo);
                    }
                    if (objetivos.isEmpty()) {
                        System.out.println("No quedan enemigos. Lleva a Thorin al cofre.");
                        break;
                    }
                    for (int i = 0; i < objetivos.size(); i++) {
                        System.out.println((i + 1) + ". " + objetivos.get(i).getEstado());
                    }
                    System.out.println("Selecciona un enemigo:");
                    Integer indice = leerEntero(entrada);
                    if (indice == null) return false;
                    if (indice < 1 || indice > objetivos.size()) {
                        System.out.println("Objetivo fuera de rango.");
                        break;
                    }
                    ejecutarEspecial(heroe, objetivos.get(indice - 1));
                    registrarBajasEnemigas();
                    actualizarTablero();
                    mostrarTablero();
                    return true;
                case 2:
                    if (usarPocion(heroe)) return true;
                    break;
                case 3:
                    System.out.println("Nueva columna X (0-4):");
                    Integer x = leerEntero(entrada);
                    if (x == null) return false;
                    System.out.println("Nueva fila Y (0-4):");
                    Integer y = leerEntero(entrada);
                    if (y == null) return false;
                    if (mover(heroe, x, y)) {
                        mostrarTablero();
                        return true;
                    }
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private Integer leerEntero(Scanner entrada) {
        return EntradaConsola.leerEntero(entrada);
    }

    private void turnoEnemigos() {
        System.out.println("\nTURNO ENEMIGO");
        for (Personaje enemigo : enemigos) {
            if (!enemigo.actualizarTurno()) {
                actualizarTablero();
                continue;
            }
            // Ataca al primer heroe vivo; no modifica listas durante el recorrido.
            for (Personaje heroe : heroes) {
                if (heroe.estaVivo()) {
                    ejecutarEspecial(enemigo, heroe);
                    actualizarTablero();
                    mostrarTablero();
                    break;
                }
            }
            if (hayDerrota()) break;
        }
    }

    public boolean usarPocion(Personaje heroe) {
        if (heroe == null || !heroes.contains(heroe) || !heroe.estaVivo()) return false;
        if (inventario.getItems().isEmpty()) {
            System.out.println("Inventario vacio.");
            return false;
        }
        Pocion pocion = inventario.obtener(0);
        pocion.usar(heroe);
        inventario.retirar(0);
        System.out.println("Pocion consumida. Restantes: " + inventario.getItems().size());
        return true;
    }

    public boolean mover(Personaje personaje, int x, int y) {
        if (personaje == null || !heroes.contains(personaje) || !personaje.estaVivo()) return false;
        if (x < 0 || x >= tablero[0].length || y < 0 || y >= tablero.length) {
            System.out.println("Movimiento fuera del tablero.");
            return false;
        }
        int distancia = Math.abs(x - personaje.getPosicionX()) + Math.abs(y - personaje.getPosicionY());
        if (distancia != 1) {
            System.out.println("Muevete una casilla horizontal o vertical.");
            return false;
        }
        actualizarTablero();
        if (tablero[y][x] != '.' && tablero[y][x] != 'C') {
            System.out.println("La casilla esta ocupada.");
            return false;
        }
        personaje.cambiarPosicion(x, y);
        actualizarTablero();
        System.out.println(personaje.getNombre() + " se mueve a (" + x + "," + y + ").");
        return true;
    }

    public void limpiarBajas() {
        escuadron.retirarCaidos();
        registrarBajasEnemigas();
        actualizarTablero();
    }

    public boolean hayVictoria() {
        boolean enemigosVivos = false;
        for (Personaje enemigo : enemigos) {
            if (enemigo.estaVivo()) enemigosVivos = true;
        }
        return !enemigosVivos && heroes.contains(principal) && principal.estaVivo()
                && principal.getPosicionX() == COFRE_X && principal.getPosicionY() == COFRE_Y;
    }

    public boolean hayDerrota() {
        int vidaTotal = 0;
        for (Personaje heroe : heroes) vidaTotal += heroe.getVida();
        return heroes.isEmpty() || vidaTotal <= 0 || !principal.estaVivo();
    }

    public void actualizarTablero() {
        for (int fila = 0; fila < tablero.length; fila++) {
            for (int columna = 0; columna < tablero[fila].length; columna++) {
                tablero[fila][columna] = '.';
            }
        }
        tablero[COFRE_Y][COFRE_X] = 'C';
        for (Personaje heroe : heroes) {
            if (heroe.estaVivo()) tablero[heroe.getPosicionY()][heroe.getPosicionX()] =
                    switch (heroe.getTipoPersonaje()) { case GUERRERO -> 'G'; case MAGO -> 'M'; case ARQUERO -> 'A'; };
        }
        for (Personaje enemigo : enemigos) {
            if (enemigo.estaVivo()) tablero[enemigo.getPosicionY()][enemigo.getPosicionX()] = 'E';
        }
    }

    public void mostrarTablero() {
        System.out.println("    0  1  2  3  4");
        for (int fila = 0; fila < tablero.length; fila++) {
            System.out.print(fila + "  ");
            for (int columna = 0; columna < tablero[fila].length; columna++) {
                System.out.print("[" + tablero[fila][columna] + "]");
            }
            System.out.println();
        }
        System.out.println("G: Guerrero | M: Mago | A: Arquero | E: Enemigo | C: Cofre | .: Vacio");
    }

    private void mostrarEstado() {
        actualizarTablero();
        mostrarTablero();
        for (Personaje heroe : heroes) System.out.println(heroe.getEstado());
        System.out.println("Enemigos: " + enemigos.size() + " | Pociones: " + inventario.getItems().size());
    }

    // Control del turno: la excepcion se captura aqui, nunca se oculta en Mago.
    private void ejecutarEspecial(Personaje actor, Personaje objetivo) {
        try {
            actor.realizarAtaqueEspecial(objetivo);
            estadisticas.incrementar("Ataques especiales");
        } catch (SinManaException e) {
            System.err.println(e.getMessage());
            System.out.println("El controlador elige un ataque basico alternativo.");
            actor.atacar(objetivo);
        }
    }

    private void registrarBajasEnemigas() {
        for (Personaje enemigo : enemigos) {
            if (!enemigo.estaVivo()) {
                bestiario.registrarVictoria(enemigo.getNombre());
                estadisticas.incrementar("Enemigos derrotados");
            }
        }
        // Cada baja sale de esta lista inmediatamente tras registrarse.
        enemigos.removeIf(p -> !p.estaVivo());
    }

    public PerfilJugador getPerfil() { return perfil; }
    public Bestiario getBestiario() { return bestiario; }
    public Estadisticas getEstadisticas() { return estadisticas; }
    public Escuadron getEscuadron() { return escuadron; }

    public boolean reclutar(Personaje p) {
        if (p == null || !p.estaVivo() || heroes.contains(p)) return false;
        actualizarTablero();
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (tablero[y][x] == '.') {
                    p.cambiarPosicion(x, y);
                    escuadron.reclutarPersonaje(p);
                    actualizarTablero();
                    return true;
                }
            }
        }
        return false;
    }

    public List<Personaje> getHeroes() { return heroes; }
    public List<Personaje> getEnemigos() { return enemigos; }
    public Inventario<Pocion> getInventario() { return inventario; }
    public char getCelda(int x, int y) { return tablero[y][x]; }
}

