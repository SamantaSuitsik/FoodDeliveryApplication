package org.example.fooddeliveryapp.repository;

import org.example.fooddeliveryapp.models.Weather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends CrudRepository<Weather, Long> {
    /**
     * Find the latest weather info for a city in the database.
     * @param cityName the city that's weather is desired
     * @return weather for the city
     */
    @Query(value = "select * from Weather where city = ?1 order by date desc limit 1", nativeQuery = true)
    Weather findByStation(String cityName);

}
