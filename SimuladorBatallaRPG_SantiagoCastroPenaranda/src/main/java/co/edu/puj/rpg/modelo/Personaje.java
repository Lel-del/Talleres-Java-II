package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

/** Encapsula el estado y las reglas del héroe, sin imprimir desde el dominio. */
public class Personaje {
    public static final double PRECIO_POCION_GRANDE = 25.0;
    public static final int CURACION_POCION_GRANDE = 40;
    public static final int EXPERIENCIA_POR_NIVEL = 100;
    private static final double PROBABILIDAD_CRITICO = 0.15;

    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;

    private int pocionesGrandes;
    private int experiencia;
    private boolean ultimoAtaqueCritico;

    // La guía solo precisa el constructor completo: estos valores son de ejemplo.
    public Personaje() {
        this("Héroe", 100, 20, 50.0);
    }

    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (vidaMaxima <= 0 || puntosAtaque <= 0) {
            throw new IllegalArgumentException("Vida máxima y ataque deben ser positivos.");
        }
        if (!Double.isFinite(oro) || oro < 0.0) {
            throw new IllegalArgumentException("El oro inicial debe ser finito y no negativo.");
        }
        this.nombre = nombre.trim();
        this.vidaMaxima = vidaMaxima;
        this.puntosAtaque = puntosAtaque;
        // El constructor también respeta las reglas de los mutadores.
        this.setPuntosVida(this.vidaMaxima);
        this.setOro(oro);
        this.setNivel(1);
        this.pocionesGrandes = 1;
        this.experiencia = 0;
        this.ultimoAtaqueCritico = false;
    }

    public final void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else {
            this.puntosVida = Math.min(vida, this.vidaMaxima);
            this.estaVivo = true;
        }
    }

    // false comunica el rechazo: Main muestra el aviso y no se entrega un ítem.
    public final boolean setOro(double oro) {
        if (!Double.isFinite(oro) || oro < 0.0) {
            return false;
        }
        this.oro = oro;
        return true;
    }

    protected final void setNivel(int nivel) {
        if (nivel >= 1) {
            this.nivel = nivel;
        }
    }

    public void atacar(Enemigo enemigo) {
        this.ultimoAtaqueCritico = false;
        if (!this.estaVivo || enemigo == null || !enemigo.isEstaVivo()) {
            return;
        }
        // Math.random produce valores en [0, 1): el intervalo [0, 0.15) es el 15%.
        this.ultimoAtaqueCritico = esGolpeCritico(Math.random());
        long dano = (long) this.puntosAtaque * (this.ultimoAtaqueCritico ? 2 : 1);
        enemigo.recibirDano((int) Math.min(dano, Integer.MAX_VALUE));
    }

    // Separa la regla de probabilidad de la fuente aleatoria para poder comprobarla.
    protected boolean esGolpeCritico(double tirada) {
        return tirada >= 0.0 && tirada < PROBABILIDAD_CRITICO;
    }

    public void usarPocion(int cantidadRestaurada) {
        if (!this.estaVivo || cantidadRestaurada <= 0 || this.pocionesGrandes == 0
                || this.puntosVida == this.vidaMaxima) {
            return;
        }
        // La suma en long impide que una curación muy grande se vuelva negativa.
        long nuevaVida = (long) this.puntosVida + cantidadRestaurada;
        this.setPuntosVida((int) Math.min(nuevaVida, this.vidaMaxima));
        this.pocionesGrandes--;
    }

    public void recibirDano(int cantidadDano) {
        if (!this.estaVivo || cantidadDano <= 0) {
            return;
        }
        long nuevaVida = (long) this.puntosVida - cantidadDano;
        this.setPuntosVida((int) Math.max(0L, nuevaVida));
    }

    public boolean comprarPocionGrande() {
        if (!this.estaVivo || this.pocionesGrandes == Integer.MAX_VALUE) {
            return false;
        }
        // Incluso un saldo insuficiente pasa por setOro: allí se rechaza.
        if (this.setOro(this.oro - PRECIO_POCION_GRANDE)) {
            this.pocionesGrandes++;
            return true;
        }
        return false;
    }

    public boolean ganarExperiencia(Enemigo enemigo) {
        if (!this.estaVivo || enemigo == null || enemigo.isEstaVivo()) {
            return false;
        }
        int recompensa = enemigo.reclamarExperiencia();
        if (recompensa <= 0) {
            return false;
        }
        long total = (long) this.experiencia + recompensa;
        long nuevoNivel = (long) this.nivel + total / EXPERIENCIA_POR_NIVEL;
        this.setNivel((int) Math.min(nuevoNivel, Integer.MAX_VALUE));
        this.experiencia = (int) (total % EXPERIENCIA_POR_NIVEL);
        return true;
    }

    public String getNombre() { return nombre; }
    public int getPuntosVida() { return puntosVida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getPuntosAtaque() { return puntosAtaque; }
    public int getNivel() { return nivel; }
    public double getOro() { return oro; }
    public boolean isEstaVivo() { return estaVivo; }
    public int getPocionesGrandes() { return pocionesGrandes; }
    public int getExperiencia() { return experiencia; }
    public boolean isUltimoAtaqueCritico() { return ultimoAtaqueCritico; }
}
