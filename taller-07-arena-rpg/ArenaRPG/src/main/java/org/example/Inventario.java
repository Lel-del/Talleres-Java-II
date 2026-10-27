package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.example.exception.CapacidadExcedidaException;

public class Inventario<T> {
    private final List<T> items = new ArrayList<>();
    private final int capacidadMaxima;
    public Inventario() { this(6); }
    public Inventario(int capacidadMaxima) {
        if (capacidadMaxima <= 0) throw new IllegalArgumentException("Capacidad debe ser positiva.");
        this.capacidadMaxima = capacidadMaxima;
    }
    public void agregar(T item) throws CapacidadExcedidaException {
        if (item == null) throw new IllegalArgumentException("El item no puede ser null.");
        if (items.size() >= capacidadMaxima) throw new CapacidadExcedidaException("Inventario lleno: " + capacidadMaxima);
        items.add(item);
    }
    public T obtener(int index) { return items.get(index); }
    public T retirar(int index) { return items.remove(index); }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public List<T> getItems() { return Collections.unmodifiableList(items); }
}
