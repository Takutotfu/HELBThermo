package com.example.helbthermo;

import java.util.HashMap;

// Design Pattern Strategy
public interface SimulationSystem {
    void simulation(HashMap<String, Cell> cells);
}

