package org.example;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.example.model.enums.TipoPersonaje;
import org.example.model.enums.EstadoPersonaje;
import org.example.exception.SinManaException;
import org.example.exception.CapacidadExcedidaException;
import org.example.exception.VidaInsuficienteException;

public abstract class Personaje implements ICombatiente {
    protected String nombre;
    protected int posicionX;
    protected int posicionY;
    private final int nivel;
    private final TipoPersonaje tipoPersonaje;
    private EstadoPersonaje estado = EstadoPersonaje.VIVO;
    private int turnosEstado;
    private final Set<String> habilidades = new HashSet<>();
    private static final int CAPACIDAD_HABILIDADES = 5;
    private Arma armaEquipada;
    private Gremio gremio;
    private final CorazonRPG corazon;

    public Personaje(String nombre, int vida, int ataqueBase, int x, int y, TipoPersonaje tipo) {
        this(nombre, vida, ataqueBase, x, y, 0, tipo);
    }

    protected Personaje(String nombre, int vida, int ataqueBase, int x, int y, int mana, TipoPersonaje tipo) {
        if (tipo == null || nombre == null || nombre.isBlank() || ataqueBase < 5 || ataqueBase > 100
                || x < 0 || x >= 5 || y < 0 || y >= 5)
            throw new IllegalArgumentException("Nombre, ataque (5..100) o posicion no validos.");
        this.tipoPersonaje = tipo;
        this.nombre = nombre;
        this.posicionX = x;
        this.posicionY = y;
        this.nivel = ataqueBase / 5;
        this.corazon = new CorazonRPG(vida, mana);
        // Compatibilidad con los ataques 16 y 18 del taller anterior.
        if (ataqueBase % 5 != 0) armaEquipada = new Arma("Arma inicial", ataqueBase % 5);
    }

    public abstract void realizarAtaqueEspecial(Personaje objetivo) throws SinManaException;
    public int calcularAtaqueTotal() {
        int ataqueBase = nivel * 5;
        if (armaEquipada != null) ataqueBase += armaEquipada.getDanioEfectivo();
        return (int) Math.round(ataqueBase + tipoPersonaje.getBonificadorAtaqueBase());
    }
    public void equiparArma(Arma nuevaArma) {
        if (nuevaArma == null) { System.out.println("Arma null rechazada. Usa desequiparArma."); return; }
        armaEquipada = nuevaArma;
        System.out.println(nombre + " equipa " + nuevaArma);
    }
    public void desequiparArma() {
        System.out.println(armaEquipada == null ? "No hay arma para retirar." : nombre + " retira su arma.");
        armaEquipada = null;
    }
    public void unirseAGremio(Gremio nuevoGremio) {
        if (nuevoGremio == null || nuevoGremio == gremio) return;
        salirDeGremio();
        gremio = nuevoGremio;
        nuevoGremio.agregarMiembro(this);
    }
    public void salirDeGremio() {
        if (gremio == null) return;
        Gremio anterior = gremio;
        gremio = null; // Rompe la llamada cruzada antes de retirar el otro extremo.
        anterior.retirarMiembro(this);
    }
    public void atacar(Personaje p) { atacar(p, false); }
    public void atacar(Personaje p, boolean modoFuria) {
        validarAccion();
        if (!estaVivo() || p == null || !p.estaVivo() || p == this) {
            System.out.println("No se puede atacar a ese objetivo."); return;
        }
        int dano = calcularAtaqueTotal();
        if (modoFuria) dano *= 2;
        System.out.println(nombre + (modoFuria ? " ataca con furia a " : " ataca a ") + p.nombre);
        p.defender(dano);
    }
    @Override
    public void defender(int danoRecibido) {
        recibirDano(Math.max(0, danoRecibido - getDefensaTipo()));
    }
    public int getDefensaTipo() { return (int) Math.round(tipoPersonaje.getBonificadorDefensaBase()); }
    public void recibirDano(int cantidad) {
        boolean vivia = estaVivo();
        int real = corazon.recibirDano(cantidad);
        System.out.println(nombre + " recibe " + real + " de dano. Vida: " + getVida());
        if (getVida() == 0) { estado = EstadoPersonaje.DERROTADO; turnosEstado = 0; }
        if (vivia && !estaVivo()) System.out.println(nombre + " ha caido.");
    }
    public void curar(int cantidad) {
        System.out.println(nombre + " recupera " + corazon.curar(cantidad) + " de vida. Vida: " + getVida());
    }
    protected boolean gastarMana(int cantidad) { return corazon.gastarMana(cantidad); }
    public int getPuntosMana() { return corazon.getManaBase(); }
    public boolean estaVivo() { return getVida() > 0; }
    public String getNombre() { return nombre; }
    public int getNivel() { return nivel; }
    public int getVida() { return corazon.getSaludBase(); }
    public Gremio getGremio() { return gremio; }
    public Arma getArmaEquipada() { return armaEquipada; }
    public int getPosicionX() { return posicionX; }
    public int getPosicionY() { return posicionY; }
    public void cambiarPosicion(int x, int y) {
        if (x < 0 || x >= 5 || y < 0 || y >= 5) throw new IllegalArgumentException("Posicion fuera del tablero.");
        posicionX = x; posicionY = y;
    }
    public TipoPersonaje getTipoPersonaje() { return tipoPersonaje; }
    public EstadoPersonaje getEstadoPersonaje() { return estado; }
    protected void validarAccion() {
        if (!estaVivo() || estado == EstadoPersonaje.ATURDIDO)
            throw new IllegalStateException("El personaje derrotado o aturdido no puede actuar.");
    }
    public void aplicarEstado(EstadoPersonaje nuevo) {
        if (nuevo == null || nuevo == EstadoPersonaje.DERROTADO)
            throw new IllegalArgumentException("DERROTADO solo se asigna al llegar a cero vida.");
        if (!estaVivo()) throw new IllegalStateException("No se pueden alterar estados de un derrotado.");
        estado = nuevo;
        turnosEstado = nuevo == EstadoPersonaje.ENVENENADO ? 2 : nuevo == EstadoPersonaje.ATURDIDO ? 1 : 0;
    }
    // Se llama una sola vez al inicio de cada turno de este personaje.
    public boolean actualizarTurno() {
        if (!estaVivo()) return false;
        if (estado == EstadoPersonaje.ATURDIDO) {
            estado = EstadoPersonaje.VIVO; turnosEstado = 0;
            System.out.println(nombre + " pierde su turno por aturdimiento.");
            return false;
        }
        if (estado == EstadoPersonaje.ENVENENADO) {
            recibirDano(5);
            if (estaVivo() && --turnosEstado == 0) estado = EstadoPersonaje.VIVO;
        }
        return estaVivo();
    }
    public boolean aprenderHabilidad(String habilidad) throws CapacidadExcedidaException {
        if (habilidad == null || habilidad.isBlank()) throw new IllegalArgumentException("Habilidad vacia.");
        String nombreHabilidad = habilidad.trim().replaceAll("\\s+", " ");
        for (String existente : habilidades) if (existente.equalsIgnoreCase(nombreHabilidad)) return false;
        if (habilidades.size() >= CAPACIDAD_HABILIDADES)
            throw new CapacidadExcedidaException("Libro lleno: " + CAPACIDAD_HABILIDADES + " habilidades.");
        return habilidades.add(nombreHabilidad);
    }
    public Set<String> obtenerHabilidades() { return Collections.unmodifiableSet(habilidades); }
    public void lanzarHechizo(Personaje objetivo, int costo) throws SinManaException {
        validarAccion();
        if (costo < 0) throw new IllegalArgumentException("El costo de mana no puede ser negativo.");
        if (objetivo == null || objetivo == this || !objetivo.estaVivo())
            throw new IllegalArgumentException("Objetivo no valido.");
        int manaActual = getPuntosMana();
        if (costo > manaActual) throw new SinManaException("Mana insuficiente: disponible " + manaActual + ", costo " + costo);
        gastarMana(costo);
        System.out.println(nombre + " lanza un hechizo. Mana restante: " + getPuntosMana());
        objetivo.defender(calcularAtaqueTotal() + 10);
    }
    public void realizarRitual(int costoVida) {
        validarAccion();
        if (costoVida < 0) throw new IllegalArgumentException("Costo de vida negativo.");
        if (costoVida >= getVida()) throw new VidaInsuficienteException("El ritual requiere conservar al menos 1 de vida.");
        recibirDano(costoVida);
        System.out.println("Ritual de practica completado.");
    }
    public String getEstado() { return nombre + " | " + corazon + " | " + estado; }
    @Override
    public String toString() {
        return new StringBuilder(getEstado()).append(" | Tipo: ").append(tipoPersonaje).append(" | Habilidades: ").append(habilidades).append(" | Nivel: ").append(nivel)
                .append(" | Ataque: ").append(calcularAtaqueTotal())
                .append(" | Arma: ").append(armaEquipada == null ? "Desarmado" : armaEquipada)
                .append(" | Gremio: ").append(gremio == null ? "Sin gremio" : gremio.getNombre())
                .append(" | Posicion: (").append(posicionX).append(",").append(posicionY).append(")").toString();
    }
}
