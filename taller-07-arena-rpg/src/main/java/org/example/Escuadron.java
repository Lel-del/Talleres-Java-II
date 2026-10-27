package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Escuadron {
    private final String nombre;
    private final List<Personaje> miembros = new ArrayList<>();
    public Escuadron(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre vacio.");
        this.nombre = nombre;
    }
    public boolean reclutarPersonaje(Personaje p) {
        if (p == null || miembros.contains(p)) return false;
        miembros.add(p);
        return true;
    }
    public void expulsarPersonaje(Personaje p) { miembros.remove(p); }
    public void disolver() { miembros.clear(); }
    public void retirarCaidos() { miembros.removeIf(p -> !p.estaVivo()); }
    public List<Personaje> getMiembros() { return Collections.unmodifiableList(miembros); }
    public String getNombre() { return nombre; }
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder("Escuadron ").append(nombre).append(": ");
        for (Personaje p : miembros) texto.append(p.getNombre()).append("; ");
        return texto.toString();
    }
}
