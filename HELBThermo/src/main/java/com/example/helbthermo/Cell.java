package com.example.helbthermo;

public class Cell {
    public static final double SIZE = 100.0;

    private final String id;
    private final int x;
    private final int y;

    private double temperature;

    public Cell(int x, int y) {
        this.id = ""+x+y;
        this.x = x;
        this.y = y;
        this.temperature = Thermo.TEMP_EXT;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public double getTemperature() { return temperature; }

    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getId() {return id;}
}
