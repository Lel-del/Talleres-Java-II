package org.example;

public final class Arma {
    private final String nombre;
    private final int danioBase;
    public Arma(String nombre, int danioBase) {
        if (nombre == null || nombre.isBlank() || danioBase < 0 || danioBase > 1000)
            throw new IllegalArgumentException("Arma no valida (dano 0..1000).");
        this.nombre = nombre;
        this.danioBase = danioBase;
    }
    public String getNombre() { return nombre; }
    public int getDanioBase() { return danioBase; }
    @Override
    public String toString() {
        return new StringBuilder(nombre).append(" (+").append(danioBase).append(")").toString();
    }
}
