package com.example.helbthermo;

public class DeadCell extends Cell{

    public DeadCell(int x, int y) {
        super(x, y);
        setTemperature(0);
    }

    // Securité pour ne pas changé de temperature
    @Override
    public void setTemperature(int temperature) {
        super.setTemperature(0);
    }
}
