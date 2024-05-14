package com.example.helbthermo;

public class HeatSourceCell extends Cell {

    private final double heatTemperature;
    private boolean isActivated = true;

    public HeatSourceCell(int x, int y, double heatTemperature) {
        super(x, y);
        setTemperature(heatTemperature);
        this.heatTemperature = heatTemperature;
    }

    public boolean isActivated() {return isActivated;}

    public void activate() {isActivated = true;}

    public void deactivate() {isActivated = false;}

    public double getHeatTemperature() {return heatTemperature;}

}
