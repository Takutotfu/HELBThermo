package com.example.helbthermo;

public class ThermoController {
    private ThermoView view;

    public ThermoController(ThermoView view){
        this.view = view;
        view.initView();
    }
}
