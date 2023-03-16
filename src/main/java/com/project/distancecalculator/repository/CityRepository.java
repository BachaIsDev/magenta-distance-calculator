package com.project.distancecalculator.repository;

import com.project.distancecalculator.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long>{
    City getCityByName(String name);
}
