package org.example.fooddeliveryapp.services;

import org.example.fooddeliveryapp.exceptions.BadParametersException;
import org.example.fooddeliveryapp.exceptions.ForbiddenVehicleException;
import org.example.fooddeliveryapp.models.Weather;
import org.example.fooddeliveryapp.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
   private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * Return delivery fee based on a given city and vehicle.
     * @param city the location where the delivery is taking place
     * @param vehicle type of vehicle that is used to deliver the food
     * @return how much the delivery costs
     */
    public double calculateFee(String city, String vehicle) {
        double fee = 0;
        String station;
        vehicle = vehicle.toLowerCase();
        city = city.toLowerCase();

        // Calculate the fee based on the vehicle type
        double vehicleFee = getVehicleFee(vehicle);

        // There are multiple stations in Tartu and Tallinn, so one is selected
        // The ride's price is dependent on the city
        switch (city) {
            case "tallinn":
                station = "Tallinn-Harku";
                fee += vehicleFee + 1;
                break;
            case "tartu":
                station = "Tartu-Tõravere";
                fee += vehicleFee + 0.5;
                break;
            case "pärnu":
                station = "Pärnu";
                fee += vehicleFee;
                break;
            default:
                throw new BadParametersException(city);
        }

        // Extra fees for weather conditions when not using a car
        if (vehicle.equals("bike") || vehicle.equals("scooter")) {
            // Acquire the newest weather info from the database for given city
            Weather weather = deliveryRepository.findByStation(station);
            fee += calculateFeeByWeather(weather, vehicle);
        }

        return fee;
    }

    private double getVehicleFee(String vehicle) {

        // The prices are calculated by Pärnu regional base fee (it's the cheapest)
        return switch (vehicle) {
            case "car" -> 3.0;
            case "scooter" -> 2.5;
            case "bike" -> 2;
            default -> throw new BadParametersException(vehicle);
        };
    }

    private double calculateFeeByWeather(Weather weather, String vehicle) {
        double extraFee = 0;
        double temperature = weather.getTemperature();
        double windSpeed = weather.getWindSpeed();
        String phenomenon = weather.getPhenomenon().toLowerCase();

        if (temperature < -10)
            extraFee = 1;
        else if (temperature <= 0)
            extraFee = 0.5;

        if (vehicle.equals("bike")) {
            if (windSpeed > 20)
                // It's not allowed to ride a bike with strong wind
                throw new ForbiddenVehicleException();
            else if (windSpeed >= 10)
                extraFee += 0.5;
        }

        // Some phenomenons may have a longer description than just one word
        if (phenomenon.contains("snow") || phenomenon.contains("sleet"))
            extraFee += 1;

        else if (phenomenon.contains("rain"))
            extraFee += 0.5;

        // It's not allowed to use a scooter or a bike with the following conditions
        if (phenomenon.equals("glaze") || phenomenon.equals("hail") || phenomenon.equals("thunder"))
            throw new ForbiddenVehicleException();

        return extraFee;
    }
}
