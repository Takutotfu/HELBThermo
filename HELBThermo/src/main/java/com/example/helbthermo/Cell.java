package com.example.helbthermo;

public class Cell {
    public static final double SIZE = 50.0;

    private final int X;
    private final int Y;

    private int temperature;

    public Cell(int x, int y) {
        this.X = x;
        this.Y = y;
        this.temperature = 0;
    }

    public int getX() { return X; }

    public int getY() { return Y; }

    public int getTemperature() { return temperature; }

    public void setTemperature(int temperature) { this.temperature = temperature; }
}
