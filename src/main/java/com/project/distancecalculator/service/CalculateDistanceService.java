package com.project.distancecalculator.service;

import com.project.distancecalculator.model.CalculateResult;
import com.project.distancecalculator.model.entity.City;
import com.project.distancecalculator.model.entity.Distance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

@Service
public class CalculateDistanceService {

    private static final int EARTH_RADIUS = 6367;

    private final DistanceService distanceService;
    private final CityService cityService;

    public CalculateDistanceService(DistanceService distanceService, CityService cityService) {
        this.distanceService = distanceService;
        this.cityService = cityService;
    }

    public List<CalculateResult> calculateDistanceByCrowFlight(List<City> from, List<City> to) {
        List<CalculateResult> resultList = new ArrayList<>();
        for (City cityFrom : from) {
            for (City cityTo : to) {
                CalculateResult result = getCalculateResultDto(cityFrom, cityTo);
                resultList.add(result);
            }
        }
        return resultList;
    }

    public List<Distance> calculateDistanceByMatrix(List<City> from, List<City> to) {
        List<Distance> resultList = new ArrayList<>();
        for (City cityFrom : from) {
            for (City cityTo : to) {
                Distance distance = distanceService.getDistanceByCityNames(cityFrom.getName(), cityTo.getName());
                resultList.add(distance);
            }
        }
        return resultList;
    }

    private CalculateResult getCalculateResultDto(City from, City to) {
        CalculateResult result = new CalculateResult();
        City dbFrom = cityService.getByName(from.getName());
        City dbTo = cityService.getByName(to.getName());
        result.setFrom(dbFrom);
        result.setTo(dbTo);

        double d2r = Math.PI / 180;
        double dlong = (dbTo.getLongitude() - dbFrom.getLongitude()) * d2r;
        double dlat = (dbTo.getLatitude() - dbFrom.getLatitude()) * d2r;
        double a = pow(sin(dlat / 2.0), 2) + cos(dbFrom.getLatitude() * d2r) * cos(dbTo.getLatitude() * d2r) * pow(sin(dlong / 2.0), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        result.setCrowFlightDistance((EARTH_RADIUS * c));
        return result;
    }

}
