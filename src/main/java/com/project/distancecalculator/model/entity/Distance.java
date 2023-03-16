package com.project.distancecalculator.model.entity;

import com.project.distancecalculator.model.entity.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "distances")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Distance {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_city")
    private City fromCity;

    @ManyToOne
    @JoinColumn(name = "to_city")
    private City toCity;

    private Double distance;
}
