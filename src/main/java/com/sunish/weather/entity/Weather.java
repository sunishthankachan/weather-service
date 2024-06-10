package com.sunish.weather.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Weather {
    @Id
    private String city;
    private String temperature;
    private String unit;
    private String date;
    private String weather;
}
