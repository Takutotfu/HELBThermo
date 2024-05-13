package com.example.helbthermo;

import java.util.ArrayList;
import java.util.List;

public class Cell implements Observable {
    private final String id;
    private final int x;
    private final int y;

    private double temperature;

    private final List<Observer> observers;

    public Cell(int x, int y) {
        this.id = ""+x+y;
        this.x = x;
        this.y = y;
        this.temperature = Thermo.TEMP_EXT;
        this.observers = new ArrayList<Observer>();
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public double getTemperature() { return temperature; }

    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getId() {return id;}

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver() {
        for (Observer o : observers) {
            o.update(this);
        }
    }
}
