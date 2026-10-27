package org.example;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public final class PerfilJugador {
    private final Set<String> logros = new TreeSet<>();
    public boolean registrarLogro(String logro) {
        if (logro == null || logro.isBlank()) throw new IllegalArgumentException("Logro vacio.");
        String nombre = logro.trim().replaceAll("\\s+", " ");
        for (String actual : logros) if (actual.equalsIgnoreCase(nombre)) return false;
        return logros.add(nombre);
    }
    public Set<String> listarLogrosOrdenados() { return Collections.unmodifiableSet(logros); }
}
