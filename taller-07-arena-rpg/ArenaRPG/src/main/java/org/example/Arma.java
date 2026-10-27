package org.example;

import org.example.model.enums.RaridadItem;

public final class Arma {
    private final String nombre;
    private final int danioBase;
    private final RaridadItem raridad;
    public Arma(String nombre, int danioBase) { this(nombre, danioBase, RaridadItem.COMUN); }
    public Arma(String nombre, int danioBase, RaridadItem raridad) {
        if (nombre == null || nombre.isBlank() || danioBase < 0 || danioBase > 1000 || raridad == null)
            throw new IllegalArgumentException("Arma no valida (dano 0..1000 y rareza obligatoria).");
        this.nombre = nombre.trim(); this.danioBase = danioBase; this.raridad = raridad;
    }
    public String getNombre() { return nombre; }
    public int getDanioBase() { return danioBase; }
    public int getDanioEfectivo() { return (int) Math.round(danioBase * raridad.getMultiplicadorStat()); }
    public RaridadItem getRaridad() { return raridad; }
    @Override
    public String toString() {
        return new StringBuilder(nombre).append(" ").append(raridad).append(" (+")
            .append(getDanioEfectivo()).append(")").toString();
    }
}
