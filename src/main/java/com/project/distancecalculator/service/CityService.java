package com.project.distancecalculator.service;

import com.project.distancecalculator.model.entity.City;
import com.project.distancecalculator.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class CityService {
    private final Logger logger = Logger.getLogger("CityService");
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getCities() {
        return cityRepository.findAll();
    }

    public City getByName(String name) {
        return cityRepository.getCityByName(name);
    }

    public void save(City city) {
        cityRepository.save(city);
    }

}
