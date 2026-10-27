package org.example;

import org.example.model.enums.TipoPersonaje;

public class Arquero extends Personaje {
    public Arquero(String nombre, int vida, int ataqueBase, int x, int y, TipoPersonaje tipo) {
        super(nombre, vida, ataqueBase, x, y, tipo);
        if (tipo != TipoPersonaje.ARQUERO) throw new IllegalArgumentException("Tipo incompatible con Arquero.");
    }
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        validarAccion();
        if (objetivo == null || objetivo == this || !objetivo.estaVivo()) {
            System.out.println("Objetivo no valido."); return;
        }
        System.out.println(nombre + " dispara una flecha precisa.");
        objetivo.defender(calcularAtaqueTotal() + 5);
    }
}
