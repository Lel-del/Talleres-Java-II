package org.example;

public final class CorazonRPG {
    private int saludBase;
    private int manaBase;
    private final int saludMaxima;

    public CorazonRPG(int saludBase, int manaBase) {
        if (saludBase <= 0 || manaBase < 0) throw new IllegalArgumentException("Salud o mana invalidos.");
        this.saludBase = saludBase;
        this.saludMaxima = saludBase;
        this.manaBase = manaBase;
    }
    public int getSaludBase() { return saludBase; }
    public int getManaBase() { return manaBase; }
    public int recibirDano(int cantidad) {
        int real = Math.min(saludBase, Math.max(0, cantidad));
        saludBase -= real;
        return real;
    }
    public int curar(int cantidad) {
        if (saludBase == 0) return 0;
        int real = Math.min(saludMaxima - saludBase, Math.max(0, cantidad));
        saludBase += real;
        return real;
    }
    public boolean gastarMana(int cantidad) {
        if (cantidad < 0 || cantidad > manaBase) return false;
        manaBase -= cantidad;
        return true;
    }
    @Override
    public String toString() {
        return new StringBuilder("Corazon: vida ").append(saludBase).append("/")
                .append(saludMaxima).append(" | Mana: ").append(manaBase).toString();
    }
}
