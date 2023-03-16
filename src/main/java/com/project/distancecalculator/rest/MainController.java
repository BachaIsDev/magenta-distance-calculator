package com.project.distancecalculator.rest;

import com.project.distancecalculator.model.CalculateResult;
import com.project.distancecalculator.service.DistanceService;
import com.project.distancecalculator.model.Cities;
import com.project.distancecalculator.model.CitiesForCalculate;
import com.project.distancecalculator.model.entity.City;
import com.project.distancecalculator.model.entity.Distance;
import com.project.distancecalculator.service.CalculateDistanceService;
import com.project.distancecalculator.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {
    private final CityService cityService;
    private final CalculateDistanceService calculateDistanceService;
    private final DistanceService distanceService;

    public MainController(CityService cityService, CalculateDistanceService calculateDistanceService, DistanceService distanceService) {
        this.cityService = cityService;
        this.calculateDistanceService = calculateDistanceService;
        this.distanceService = distanceService;
    }

    @GetMapping("/cities")
    public ResponseEntity<?> getCities() {
        Map<String, Object> map = new LinkedHashMap<>();
        List<City> cities = cityService.getCities();
        if (!cities.isEmpty()) {
            map.put("status", 1);
            map.put("data", cities);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("status", 0);
            map.put("message", "Data is not found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createCities(@RequestParam("file") MultipartFile multiPart) {
        List<City> cityList = null;
        try{
            //unmarshall xml to List<City> cityList and save in the database
            JAXBContext jaxbContext = JAXBContext.newInstance(Cities.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Cities citiesWrapper = (Cities) unmarshaller.unmarshal(multiPart.getInputStream());
            cityList = citiesWrapper.getCities();
            cityList.forEach(System.out::println);
            citiesWrapper.getCities().forEach(cityService::save);

            // create distances between cities and save in the database
            for(int i = 0; i < cityList.size(); i++){
                for (int j = i; j < cityList.size(); j++) {
                    String fromName = cityList.get(i).getName();
                    String toName = cityList.get(j).getName();

                    if (!fromName.equals(toName)) {
                        Distance distance = new Distance();
                        City fromCity = cityService.getByName(fromName);
                        City toCity = cityService.getByName(toName);
                        distance.setFromCity(fromCity);
                        distance.setToCity(toCity);
                        distance.setDistance(distanceService.calculateMatrixDistance(fromCity, toCity));
                        distanceService.saveDistance(distance);
                    }
                }
            }
        }catch (IOException | JAXBException e){
            e.getStackTrace();
        }

        return new ResponseEntity<>(cityList, HttpStatus.OK);
    }

    @PostMapping(value = "/calculate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateDistance(@RequestBody CitiesForCalculate citiesForCalculate) {
        Map<String, Object> map = new LinkedHashMap<>();
        CalculateResult error = new CalculateResult();
        if (citiesForCalculate.way == null) {
            error.setError("Wrong input data!");
            map.put("status", 0);
            map.put("error", error);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        // get a distance data by the way of calculation
        switch (citiesForCalculate.way) {
            case CROWFLIGHT: {
                List<CalculateResult> calculateResultList = calculateDistanceService.calculateDistanceByCrowFlight(citiesForCalculate.from, citiesForCalculate.to);
                map.put("status", 1);
                map.put("distance", calculateResultList);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            case MATRIX: {
                List<Distance> calculateDistanceList = calculateDistanceService.calculateDistanceByMatrix(citiesForCalculate.from, citiesForCalculate.to);
                map.put("status", 1);
                map.put("distance", calculateDistanceList);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
        error.setError("No such calculate way");
        map.put("status", 0);
        map.put("error", error);
        return new ResponseEntity<>(map, HttpStatus.EXPECTATION_FAILED);
    }
}
