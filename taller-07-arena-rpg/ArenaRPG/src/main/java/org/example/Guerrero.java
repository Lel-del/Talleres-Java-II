package org.example;

import org.example.model.enums.TipoPersonaje;

public class Guerrero extends Personaje {
    protected int fuerzaEscudo;

    public Guerrero(String nombre, int vida, int ataqueBase, int x, int y, int fuerzaEscudo, TipoPersonaje tipo) {
        super(nombre, vida, ataqueBase, x, y, tipo);
        if (tipo != TipoPersonaje.GUERRERO) throw new IllegalArgumentException("Tipo incompatible con Guerrero.");
        this.fuerzaEscudo = Math.max(0, fuerzaEscudo);
    }

    @Override
    public void defender(int danoRecibido) {
        int bloqueo = Math.min(Math.max(0, danoRecibido), fuerzaEscudo + getDefensaTipo());
        System.out.println(nombre + " bloquea " + bloqueo + " con su escudo.");
        recibirDano(Math.max(0, danoRecibido) - bloqueo);
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        atacar(objetivo, true);
    }
}
