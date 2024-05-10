package com.example.helbthermo;

public class Cell {
    public static final double SIZE = 100.0;

    private final int X;
    private final int Y;

    private double temperature;

    public Cell(int x, int y) {
        this.X = x;
        this.Y = y;
        this.temperature = Thermo.TEMP_EXT;
    }

    public int getX() { return X; }

    public int getY() { return Y; }

    public double getTemperature() { return temperature; }

    public void setTemperature(double temperature) { this.temperature = temperature; }
}
