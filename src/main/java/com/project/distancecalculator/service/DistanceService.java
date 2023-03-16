package com.project.distancecalculator.service;

import com.project.distancecalculator.model.entity.City;
import com.project.distancecalculator.model.entity.Distance;
import com.project.distancecalculator.repository.DistanceRepository;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.*;
import static java.lang.Math.sqrt;

@Service
public class DistanceService {
    private final DistanceRepository distanceRepository;
    private final Logger logger = Logger.getLogger("distanceService");

    public DistanceService(DistanceRepository distanceRepository) {
        this.distanceRepository = distanceRepository;
    }

    public Distance getDistanceByCities(City from, City to) {
        return distanceRepository.getDistanceByFromCityAndToCity(from, to);
    }

    public Distance getDistanceByCityNames(String fromName, String toName){
        if(distanceRepository.getDistanceByFromCity_NameAndToCity_Name(fromName, toName) == null){
            if(distanceRepository.getDistanceByFromCity_NameAndToCity_Name(toName, fromName) == null){
                logger.log(Level.WARNING, "No such distance in database");
                return new Distance();
            }
            return distanceRepository.getDistanceByFromCity_NameAndToCity_Name(toName, fromName);
        }
        return distanceRepository.getDistanceByFromCity_NameAndToCity_Name(fromName, toName);
    }

    public List<Distance> getAllDistances(){
        return distanceRepository.findAll();
    }

    public void saveDistance(Distance distance){
        distanceRepository.save(distance);
    }

    public Double calculateMatrixDistance(City from, City to){
        double d2r = Math.PI / 180;
        double dlong = (to.getLongitude() - from.getLongitude()) * d2r;
        double dlat = (to.getLatitude() - from.getLatitude()) * d2r;
        double a = pow(sin(dlat / 2.0), 2) + cos(from.getLatitude() * d2r) * cos(to.getLatitude() * d2r) * pow(sin(dlong / 2.0), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return 6367*c;
    }
}
