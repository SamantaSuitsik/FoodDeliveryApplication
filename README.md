# Food Delivery Application Demo
This is a sub-functionality of a food delivery application, which calculates the delivery fee for food couriers based on regional base fee, vehicle type and weather conditions.

# How to use
Run the FoodDeliveryApplication.java file  
The application is available on localhost:8080

One endpoint is at localhost:8080/delivery/fee
Possible parameters are:
city = pärnu, tartu, tallinn
vehicle = bike, car, scooter

# Usage example
http://localhost:8080/delivery/fee?city=Pärnu&vehicle=scooter
