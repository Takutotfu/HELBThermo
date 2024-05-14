package com.example.helbthermo;

import java.util.HashMap;

public class ThermoSystemTarget extends ThermoSystemManual implements SimulationSystem {

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
                if (avgTemp < 20.0) {
                    heatSourceCell.activate();
                    System.out.println("Activated heat source");
                } else {
                    heatSourceCell.deactivate();
                    System.out.println("Deactivated heat source cell");
                }
                heatSourceCell.notifyObserver();
            }
        }
    }

}
