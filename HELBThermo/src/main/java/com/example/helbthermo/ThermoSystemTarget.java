package com.example.helbthermo;

import java.util.HashMap;

public class ThermoSystemTarget extends ThermoSystemManual implements SimulationSystem {

    private final double targetTemperature = 20.0;
    private double avgTemp;

    @Override
    public void simulation(HashMap<String, Cell> cells) {
        super.simulation(cells);

        for (Cell c : cells.values()) {
            avgTemp += c.getTemperature();
        }

        avgTemp /= cells.size();

        for (Cell c : cells.values()) {
            if (c instanceof HeatSourceCell) {
                HeatSourceCell heatSourceCell = (HeatSourceCell) c;
                if (avgTemp < targetTemperature) {
                    heatSourceCell.activate();
                } else {
                    heatSourceCell.deactivate();
                }
                heatSourceCell.notifyObserver();
            }
        }
    }

}
