package org.example;

import java.util.ArrayList;
import java.util.List;

public class Inventario<T> {
    private final List<T> items = new ArrayList<>();

    public void agregar(T item) {
        if (item == null) throw new IllegalArgumentException("El item no puede ser null.");
        items.add(item);
    }
    public T obtener(int index) { return items.get(index); }
    public List<T> getItems() { return items; }
}
