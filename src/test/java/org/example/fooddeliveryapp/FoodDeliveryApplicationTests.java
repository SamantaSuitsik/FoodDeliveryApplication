package org.example.fooddeliveryapp;

import org.example.fooddeliveryapp.controllers.DeliveryController;
import org.example.fooddeliveryapp.exceptions.BadParametersException;
import org.example.fooddeliveryapp.exceptions.ForbiddenVehicleException;
import org.example.fooddeliveryapp.models.Weather;
import org.example.fooddeliveryapp.repository.DeliveryRepository;
import org.example.fooddeliveryapp.services.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class FoodDeliveryApplicationTests {

    @Autowired
    private DeliveryController deliveryController;
    private DeliveryRepository deliveryRepository;

    @BeforeEach
    public void setUp() {
        deliveryRepository = mock(DeliveryRepository.class);
        deliveryController = new DeliveryController(new DeliveryService(deliveryRepository));
    }

    @Test
    void tartuBikeTest() {
        Weather weather = new Weather("Tartu-Tõravere",26242,-2.1,4.7,"light snow shower",new Timestamp(1711619168L *1000));
        Mockito.when(deliveryRepository.findByStation(Mockito.anyString())).thenReturn(weather);
        var responseEntity = deliveryController.getFee("Tartu","bike");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(4,responseEntity.getBody());
    }

    @Test
    void tallinnScooterTest() {
        Weather weather = new Weather("Tallinn-Harku",26038,10,4.7,"RAIN",new Timestamp(1711619168L *1000));
        Mockito.when(deliveryRepository.findByStation(Mockito.anyString())).thenReturn(weather);
        var responseEntity = deliveryController.getFee("tallinn","Scooter");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(4,responseEntity.getBody());
    }

    @Test
    void pärnuCarTest() {
        var responseEntity = deliveryController.getFee("PÄRNU","Car");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3,responseEntity.getBody());

    }

    @Test
    void sleetMediumWindAndColdTemperatureTest() {
        Weather weather = new Weather("Pärnu",41803,-11,20.0,"sleet",new Timestamp(1711619168L *1000));
        Mockito.when(deliveryRepository.findByStation(Mockito.anyString())).thenReturn(weather);
        var responseEntity = deliveryController.getFee("pärnu","bike");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(4.5,responseEntity.getBody());
    }

    @Test
    void bikeAndStrongWindTest() {
        Weather weather = new Weather("Tartu-Tõravere",26242,-2.1,21,"light snow shower",new Timestamp(1711619168L *1000));
        Mockito.when(deliveryRepository.findByStation(Mockito.anyString())).thenReturn(weather);

        assertThrows(ForbiddenVehicleException.class,() -> deliveryController.getFee("Tartu", "bike"));
    }

    @Test
    void thunderAndBikeTest() {
        Weather weather = new Weather("Tallinn-Harku",26038,10,4.7,"thunder",new Timestamp(1711619168L *1000));
        Mockito.when(deliveryRepository.findByStation(Mockito.anyString())).thenReturn(weather);

        assertThrows(ForbiddenVehicleException.class,() -> deliveryController.getFee("Tallinn","bike"));
    }

    @Test
    void invalidCityTest() {
        assertThrows(BadParametersException.class,() -> deliveryController.getFee("Põlva","car"));
    }

    @Test
    void invalidVehicleTest() {
        assertThrows(BadParametersException.class,() -> deliveryController.getFee("Tartu",""));
    }





}
