package com.example.helbthermo;

public class ThermoController {

    private final int ROW_CELL = 12;
    private final int COLUMN_CELL = 12;

    private ThermoView view;

    public ThermoController(ThermoView view){
        this.view = view;
        view.initView(ROW_CELL, COLUMN_CELL);
    }
}
