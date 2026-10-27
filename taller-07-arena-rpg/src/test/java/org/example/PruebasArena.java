package org.example;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class PruebasArena {
    private static int aprobadas;
    private static void verificar(boolean condicion, String nombre) {
        if (!condicion) throw new AssertionError(nombre);
        aprobadas++;
        System.out.println("OK: " + nombre);
    }
    public static void main(String[] args) {
        Guerrero g = new Guerrero("G", 100, 15, 0, 0, 10);
        Mago m = new Mago("M", 100, 20, 1, 1, 10);
        g.defender(16);
        verificar(g.getVida() == 94, "Escudo reduce dano");
        g.defender(2);
        verificar(g.getVida() == 94, "Escudo evita dano negativo");
        g.atacar(m);
        verificar(m.getVida() == 85, "Sobrecarga ataque normal");
        g.atacar(m, true);
        verificar(m.getVida() == 55, "Sobrecarga furia");
        m.realizarAtaqueEspecial(g);
        verificar(m.getPuntosMana() == 0 && g.getVida() == 74, "Hechizo consume mana y aplica defensa");
        m.realizarAtaqueEspecial(g);
        verificar(m.getPuntosMana() == 0 && g.getVida() == 64, "Sin mana usa ataque basico");
        m.realizarAtaqueEspecial(null);
        verificar(m.getPuntosMana() == 0, "Objetivo null no consume mana");
        m.recibirDano(999);
        g.atacar(m);
        verificar(m.getVida() == 0 && !m.estaVivo(), "Vida minima y objetivo derrotado");
        g.curar(999);
        verificar(g.getVida() == 100, "Curacion limitada a vida maxima");
        Arena a = new Arena();
        Personaje principal = a.getHeroes().get(0);
        principal.recibirDano(40);
        verificar(a.usarPocion(principal) && principal.getVida() == 115
            && a.getInventario().getItems().size() == 2, "Pocion cura y se consume");
        a.usarPocion(principal); a.usarPocion(principal);
        verificar(!a.usarPocion(principal), "Inventario vacio no falla");
        verificar(!a.mover(principal, -1, 0) && !a.mover(principal, 5, 0), "Limites del tablero");
        verificar(!a.mover(principal, 2, 0) && !a.mover(principal, 1, 1), "No saltos ni diagonales");
        verificar(a.mover(principal, 1, 0) && a.getCelda(0, 0) == '.'
            && a.getCelda(1, 0) == 'G', "Movimiento sincroniza tablero");
        verificar(!a.mover(principal, 1, 1), "Casilla ocupada por aliado");
        verificar(a.mover(principal, 2, 0) && a.mover(principal, 3, 0)
            && !a.mover(principal, 4, 0), "Casilla ocupada por enemigo");
        for (Personaje enemigo : a.getEnemigos()) enemigo.recibirDano(999);
        a.limpiarBajas();
        verificar(a.getEnemigos().isEmpty() && a.getCelda(4, 0) == '.'
            && !a.hayVictoria(), "Bajas eliminadas; falta cofre");
        verificar(a.mover(principal, 2, 0) && a.mover(principal, 2, 1)
            && a.mover(principal, 2, 2) && a.hayVictoria(), "Victoria requiere enemigos y cofre");
        Arena b = new Arena();
        b.mover(b.getHeroes().get(0), 1, 0);
        b.mover(b.getHeroes().get(0), 2, 0);
        b.mover(b.getHeroes().get(0), 2, 1);
        b.mover(b.getHeroes().get(0), 2, 2);
        verificar(!b.hayVictoria(), "Cofre con enemigos no da victoria");
        b.getHeroes().get(0).recibirDano(999);
        verificar(b.hayDerrota(), "Caida del principal termina mision");
        for (Personaje heroe : b.getHeroes()) heroe.recibirDano(999);
        b.limpiarBajas();
        verificar(b.hayDerrota() && b.getHeroes().isEmpty(), "Derrota escuadron vacio");
        PrintStream original = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(salida));
            new Arena().jugar(new Scanner("hola\n9\n1\n99\n"));
        } finally { System.setOut(original); }
        String texto = salida.toString();
        verificar(texto.contains("numero entero") && texto.contains("Opcion no valida")
            && texto.contains("fuera de rango") && texto.contains("interrumpida"), "Entradas invalidas y fin de entrada");
        // Partida real: ambos heroes atacan; despues se desplazan hasta el cofre.
        String partida = "1\n1\n1\n1\n1\n1\n1\n1\n2\n1\n1\n1\n1\n1\n1\n1\n1\n3\n1\n2\n3\n1\n0\n3\n1\n3\n3\n2\n0\n3\n1\n4\n3\n2\n1\n3\n0\n4\n3\n2\n2\n";
        salida.reset();
        try {
            System.setOut(new PrintStream(salida));
            new Arena().jugar(new Scanner(partida));
        } finally { System.setOut(original); }
        if (!salida.toString().contains("VICTORIA:")) System.out.println(salida); verificar(salida.toString().contains("VICTORIA:"), "Partida completa por menu hasta victoria");
        salida.reset();
        try {
            System.setOut(new PrintStream(salida));
            new Arena().jugar(new Scanner("1\n1\n".repeat(8)));
        } finally { System.setOut(original); }
        verificar(salida.toString().contains("DERROTA:"), "Partida completa por menu hasta derrota");
        System.out.println("RESULTADO: " + aprobadas + " comprobaciones aprobadas.");
    }
}




