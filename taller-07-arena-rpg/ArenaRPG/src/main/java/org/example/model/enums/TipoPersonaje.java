package org.example.model.enums;

public enum TipoPersonaje {
    GUERRERO(1.0, 1.0), MAGO(2.0, 0.0), ARQUERO(1.5, 1.5);
    private final double bonificadorAtaqueBase;
    private final double bonificadorDefensaBase;
    private TipoPersonaje(double ataque, double defensa) {
        bonificadorAtaqueBase = ataque;
        bonificadorDefensaBase = defensa;
    }
    public double getBonificadorAtaqueBase() { return bonificadorAtaqueBase; }
    public double getBonificadorDefensaBase() { return bonificadorDefensaBase; }
}
