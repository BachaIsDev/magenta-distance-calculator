package com.project.distancecalculator.model;

import com.project.distancecalculator.model.entity.City;

import javax.xml.bind.annotation.*;
import java.util.List;

// wrapper for parsing xml to java object
@XmlRootElement(name  = "cities")
@XmlSeeAlso(City.class)
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Cities  {

    private List<City> cities;

    public List<City> getCities() {
        return cities;
    }

    @XmlElement(name = "city")
    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}