package com.mycompany.lab;

// Un nodo tiene un nombre y una posición en el dibujo.
public class Nodo {
    private final String nombre;
    private int x;
    private int y;

    public Nodo(String nombre, int x, int y) {
        this.nombre = nombre;
        this.x = x;
        this.y = y;
    }

    public String getNombre() {
        return nombre;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
