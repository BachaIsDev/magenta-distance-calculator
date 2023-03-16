package com.project.distancecalculator.repository;

import com.project.distancecalculator.model.entity.Distance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistanceRepository extends JpaRepository<Distance, Long> {
    Distance getDistanceByFromCity_NameAndToCity_Name(String fromCityName, String toCityName);

}
