package co.edu.puj.rpg.modelo.enemigo;

import co.edu.puj.rpg.modelo.Personaje;

/** Este subpaquete es un paquete diferente: se interactúa mediante la API pública. */
public class Enemigo {
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private boolean estaVivo;
    private int experienciaRecompensa;
    private boolean experienciaReclamada;

    public Enemigo(String nombre, int vidaMaxima, int puntosAtaque,
                   int experienciaRecompensa) {
        if (nombre == null || nombre.trim().isEmpty() || vidaMaxima <= 0
                || puntosAtaque <= 0 || experienciaRecompensa < 0) {
            throw new IllegalArgumentException("Los datos iniciales del enemigo no son válidos.");
        }
        this.nombre = nombre.trim();
        this.vidaMaxima = vidaMaxima;
        this.puntosAtaque = puntosAtaque;
        this.experienciaRecompensa = experienciaRecompensa;
        this.experienciaReclamada = false;
        this.setPuntosVida(this.vidaMaxima);
    }

    private void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else {
            this.puntosVida = Math.min(vida, this.vidaMaxima);
            this.estaVivo = true;
        }
    }

    public void recibirDano(int cantidadDano) {
        if (!this.estaVivo || cantidadDano <= 0) {
            return;
        }
        long nuevaVida = (long) this.puntosVida - cantidadDano;
        this.setPuntosVida((int) Math.max(0L, nuevaVida));
    }

    public void atacar(Personaje personaje) {
        if (this.estaVivo && personaje != null && personaje.isEstaVivo()) {
            personaje.recibirDano(this.puntosAtaque);
        }
    }

    // El propio enemigo impide entregar dos veces la recompensa por su derrota.
    public int reclamarExperiencia() {
        if (this.estaVivo || this.experienciaReclamada) {
            return 0;
        }
        this.experienciaReclamada = true;
        return this.experienciaRecompensa;
    }

    public String getNombre() { return nombre; }
    public int getPuntosVida() { return puntosVida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getPuntosAtaque() { return puntosAtaque; }
    public boolean isEstaVivo() { return estaVivo; }
    public int getExperienciaRecompensa() { return experienciaRecompensa; }
}
