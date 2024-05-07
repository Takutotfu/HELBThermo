package com.example.helbthermo;

public class HeatSourceCell extends Cell {

    public HeatSourceCell(int x, int y, int temperature) {
        super(x, y);
        setTemperature(temperature);
    }
}
