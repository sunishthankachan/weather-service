package com.sunish.weather.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    private String city;
    private String temperature;
    private String unit;
    private String date;
    private String weather;
}
