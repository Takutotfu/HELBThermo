package com.example.helbthermo;

public class HeatSourceCell extends Cell {

    private boolean isActivated = true;

    public HeatSourceCell(int x, int y, int temperature) {
        super(x, y);
        setTemperature(temperature);
    }

    public boolean isActivated() {return isActivated;}

    public void setActivated(boolean activated) {isActivated = activated;}

    @Override
    public double getTemperature() {
        if (isActivated) {
            return super.getTemperature();
        } else {
            return 0;
        }
    }
}
