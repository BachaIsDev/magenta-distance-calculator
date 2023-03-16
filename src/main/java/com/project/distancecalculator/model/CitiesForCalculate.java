package com.project.distancecalculator.model;

import com.project.distancecalculator.model.entity.CalculationWay;
import com.project.distancecalculator.model.entity.City;

import java.util.List;

// wrapper for input data in "/calculate" endpoint
public class CitiesForCalculate {

    public CalculationWay way;

    public List<City> from;

    public List<City> to;

    public List<City> getFrom() {
        return from;
    }

    public List<City> getTo() {
        return to;
    }
}
