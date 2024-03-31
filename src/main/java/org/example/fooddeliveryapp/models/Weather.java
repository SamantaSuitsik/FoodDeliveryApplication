package org.example.fooddeliveryapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class Weather {
    @Id
    @GeneratedValue
    private Long id;
    private String city;
    private int wmo;
    private double temperature;
    private double windSpeed;
    private String phenomenon;
    private Timestamp date;

    public Weather(String city, int wmo, double temperature, double windSpeed, String phenomenon, Timestamp date) {
        this.city = city;
        this.wmo = wmo;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.phenomenon = phenomenon;
        this.date = date;
    }

}
