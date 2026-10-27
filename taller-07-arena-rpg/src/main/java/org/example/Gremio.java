package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Gremio {
    private final String nombre;
    private final List<Personaje> miembros = new ArrayList<>();
    public Gremio(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre vacio.");
        this.nombre = nombre;
    }
    public void agregarMiembro(Personaje p) {
        if (p == null) return;
        if (p.getGremio() != this) p.unirseAGremio(this);
        if (!miembros.contains(p)) miembros.add(p);
    }
    public void retirarMiembro(Personaje p) {
        if (p == null) return;
        miembros.remove(p);
        if (p.getGremio() == this) p.salirDeGremio();
    }
    public String getNombre() { return nombre; }
    public List<Personaje> getMiembros() { return Collections.unmodifiableList(miembros); }
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder("Gremio ").append(nombre).append(": ");
        for (Personaje p : miembros) texto.append(p.getNombre()).append("; ");
        return texto.toString();
    }
}
