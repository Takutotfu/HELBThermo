package com.example.helbthermo;

import java.util.ArrayList;
import java.util.List;

// Modele representant une cellule du system
public class Cell implements Observable {
    // Attributs de Cell
    private final List<Observer> observers;

    private final String id;
    private final int x;
    private final int y;

    private double temperature;

    // Constructeur
    public Cell(int x, int y) {
        this.id = ""+x+y;
        this.x = x;
        this.y = y;
        this.temperature = ThermoController.tempExt;
        this.observers = new ArrayList<>();
    }

    // Implementation d'Observable
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

    // Getter & Setter
    public int getX() { return x; }
    public int getY() { return y; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public String getId() {return id;}
}
