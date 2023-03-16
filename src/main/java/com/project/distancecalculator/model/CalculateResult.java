package com.project.distancecalculator.model;

import com.project.distancecalculator.model.entity.City;

//
public class CalculateResult {
    private Double crowFlightDistance;
    private Double matrixDistance;
    private String error;
    private City from;
    private City to;

    public City getFrom() {
        return from;
    }

    public void setFrom(City from) {
        this.from = from;
    }

    public City getTo() {
        return to;
    }

    public void setTo(City to) {
        this.to = to;
    }

    public Double getCrowFlightDistance() {
        return crowFlightDistance;
    }

    public void setCrowFlightDistance(Double crowFlightDistance) {
        this.crowFlightDistance = crowFlightDistance;
    }

    public Double getMatrixDistance() {
        return matrixDistance;
    }

    public void setMatrixDistance(Double matrixDistance) {
        this.matrixDistance = matrixDistance;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
