package org.example;

import org.example.exception.SinManaException;
import org.example.model.enums.TipoPersonaje;

public class Mago extends Personaje {
    public Mago(String nombre, int vida, int ataqueBase, int x, int y, int mana, TipoPersonaje tipo) {
        super(nombre, vida, ataqueBase, x, y, mana, tipo);
        if (tipo != TipoPersonaje.MAGO) throw new IllegalArgumentException("Tipo incompatible con Mago.");
    }
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) throws SinManaException {
        if (objetivo == null || !objetivo.estaVivo() || objetivo == this) {
            System.out.println("No se puede lanzar el hechizo a ese objetivo."); return;
        }
        lanzarHechizo(objetivo, 10);
    }
}
