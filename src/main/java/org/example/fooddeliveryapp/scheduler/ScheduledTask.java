package org.example.fooddeliveryapp.scheduler;

import org.example.fooddeliveryapp.services.WeatherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private final WeatherService weatherService;

    public ScheduledTask(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Requests weather data once every hour, 15 minutes after a full hour.
     * Weather data is taken from Estonian Environment Agency.
     * @see <a href=ilmateenistus.ee>ilmateenistus.ee</a>
     * @throws Exception if the file is not found or DocumentBuilder cannot be created
     */
    @Scheduled(cron = "${cron.expression}")
    public void fetch() throws Exception {
        String url = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
        weatherService.fetchAndParseXml(url);
    }
}
