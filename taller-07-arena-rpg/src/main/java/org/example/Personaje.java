package org.example;

public abstract class Personaje implements ICombatiente {
    protected String nombre;
    protected int posicionX;
    protected int posicionY;
    private final int nivel;
    private Arma armaEquipada;
    private Gremio gremio;
    private final CorazonRPG corazon;

    public Personaje(String nombre, int vida, int ataqueBase, int x, int y) {
        this(nombre, vida, ataqueBase, x, y, 0);
    }

    protected Personaje(String nombre, int vida, int ataqueBase, int x, int y, int mana) {
        if (nombre == null || nombre.isBlank() || ataqueBase < 5 || ataqueBase > 100
                || x < 0 || x >= 5 || y < 0 || y >= 5)
            throw new IllegalArgumentException("Nombre, ataque (5..100) o posicion no validos.");
        this.nombre = nombre;
        this.posicionX = x;
        this.posicionY = y;
        this.nivel = ataqueBase / 5;
        this.corazon = new CorazonRPG(vida, mana);
        // Compatibilidad con los ataques 16 y 18 del taller anterior.
        if (ataqueBase % 5 != 0) armaEquipada = new Arma("Arma inicial", ataqueBase % 5);
    }

    public abstract void realizarAtaqueEspecial(Personaje objetivo);
    public int calcularAtaqueTotal() {
        int ataqueBase = nivel * 5;
        if (armaEquipada != null) ataqueBase += armaEquipada.getDanioBase();
        return ataqueBase;
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
        if (!estaVivo() || p == null || !p.estaVivo() || p == this) {
            System.out.println("No se puede atacar a ese objetivo."); return;
        }
        int dano = calcularAtaqueTotal();
        if (modoFuria) dano *= 2;
        System.out.println(nombre + (modoFuria ? " ataca con furia a " : " ataca a ") + p.nombre);
        p.defender(dano);
    }
    @Override
    public void defender(int danoRecibido) { recibirDano(danoRecibido); }
    public void recibirDano(int cantidad) {
        boolean vivia = estaVivo();
        int real = corazon.recibirDano(cantidad);
        System.out.println(nombre + " recibe " + real + " de dano. Vida: " + getVida());
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
    public String getEstado() { return nombre + " | " + corazon; }
    @Override
    public String toString() {
        return new StringBuilder(getEstado()).append(" | Nivel: ").append(nivel)
                .append(" | Ataque: ").append(calcularAtaqueTotal())
                .append(" | Arma: ").append(armaEquipada == null ? "Desarmado" : armaEquipada)
                .append(" | Gremio: ").append(gremio == null ? "Sin gremio" : gremio.getNombre())
                .append(" | Posicion: (").append(posicionX).append(",").append(posicionY).append(")").toString();
    }
}
