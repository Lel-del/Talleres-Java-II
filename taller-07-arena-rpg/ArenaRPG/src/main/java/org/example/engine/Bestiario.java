package org.example.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import org.example.PerfilJugador;

public final class Bestiario {
    private final Map<String, Integer> derrotas = new HashMap<>();
    private final PerfilJugador perfil;
    public Bestiario(PerfilJugador perfil) {
        if (perfil == null) throw new IllegalArgumentException("Perfil requerido.");
        this.perfil = perfil;
    }
    private String clave(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Enemigo vacio.");
        return nombre.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
    public void registrarVictoria(String nombre) {
        String enemigo = clave(nombre);
        boolean primera = !derrotas.containsKey(enemigo);
        derrotas.put(enemigo, derrotas.getOrDefault(enemigo, 0) + 1);
        if (primera) perfil.registrarLogro("Primera victoria: " + enemigo);
    }
    public int consultar(String nombre) { return derrotas.getOrDefault(clave(nombre), 0); }
    public Map<String, Integer> obtenerDerrotas() { return Collections.unmodifiableMap(derrotas); }
    public void mostrar() {
        for (Map.Entry<String, Integer> entry : derrotas.entrySet())
            System.out.printf("%-20s %5d%n", entry.getKey(), entry.getValue());
    }
}
