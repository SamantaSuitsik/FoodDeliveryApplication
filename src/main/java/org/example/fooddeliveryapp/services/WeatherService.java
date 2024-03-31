package org.example.fooddeliveryapp.services;

import org.example.fooddeliveryapp.models.Weather;
import org.example.fooddeliveryapp.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.sql.Timestamp;

@Service
public class WeatherService {

    private final DeliveryRepository deliveryRepository;

    public WeatherService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * Fetch weather data from a given XML file in the internet, parse it
     * and add the data to a database.
     * @param url the URL link for an XML file
     * @throws Exception if the file is not found or DocumentBuilder cannot be created
     */
    public void fetchAndParseXml(String url) throws Exception {
        // Set up DOM Parser to process a given file from internet
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(url).openStream());
        doc.getDocumentElement().normalize(); // provides clean structure

        Timestamp timestamp = new Timestamp(Long.parseLong(doc.getElementsByTagName("observations").item(0).getAttributes().item(0).getTextContent())*1000);

        // Find stations that are of interest
        NodeList nodeList = doc.getElementsByTagName("station");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element station = (Element) nodeList.item(i);
            NodeList stationInfo = station.getElementsByTagName("name");
            String city = stationInfo.item(0).getTextContent();

            if (city.equals("Tartu-Tõravere") || city.equals("Tallinn-Harku") || city.equals("Pärnu")) {

                // Retrieve desired attributes for the station
                double temperature = Double.parseDouble(station.getElementsByTagName("airtemperature").item(0).getTextContent());
                int wmo = Integer.parseInt(station.getElementsByTagName("wmocode").item(0).getTextContent());
                double windSpeed = Double.parseDouble(station.getElementsByTagName("windspeed").item(0).getTextContent());
                String phenomenon = station.getElementsByTagName("phenomenon").item(0).getTextContent();

                // Add city's weather info to the database
                deliveryRepository.save(new Weather(city,wmo,temperature,windSpeed,phenomenon,timestamp));
            }
        }

    }
}
