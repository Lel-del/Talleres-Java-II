package org.example;

import org.example.model.enums.RaridadItem;

public class Pocion {
    private final int curacion;
    private final RaridadItem raridad;
    public Pocion(int curacion) { this(curacion, RaridadItem.COMUN); }
    public Pocion(int curacion, RaridadItem raridad) {
        if (curacion <= 0 || curacion > 1000 || raridad == null)
            throw new IllegalArgumentException("Curacion 1..1000 y rareza obligatoria.");
        this.curacion = curacion; this.raridad = raridad;
    }
    public void usar(Personaje personaje) {
        if (personaje == null) { System.out.println("No hay personaje para curar."); return; }
        personaje.curar(getCuracion());
    }
    public int getCuracionBase() { return curacion; }
    public int getCuracion() { return (int) Math.round(curacion * raridad.getMultiplicadorStat()); }
    public RaridadItem getRaridad() { return raridad; }
}
