package org.example;

public class Guerrero extends Personaje {
    protected int fuerzaEscudo;

    public Guerrero(String nombre, int vida, int ataqueBase, int x, int y, int fuerzaEscudo) {
        super(nombre, vida, ataqueBase, x, y);
        this.fuerzaEscudo = Math.max(0, fuerzaEscudo);
    }

    @Override
    public void defender(int danoRecibido) {
        int bloqueo = Math.min(Math.max(0, danoRecibido), fuerzaEscudo);
        System.out.println(nombre + " bloquea " + bloqueo + " con su escudo.");
        recibirDano(Math.max(0, danoRecibido) - bloqueo);
    }

    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        atacar(objetivo, true);
    }
}
