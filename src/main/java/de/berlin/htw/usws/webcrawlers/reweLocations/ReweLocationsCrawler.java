package de.berlin.htw.usws.webcrawlers.reweLocations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ejb.Stateless;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Stateless
public class ReweLocationsCrawler {

    private static final String REWE_GEO_URL = "https://meinmarkt.rewe.de/partner3/content/marketsearch?searchString=berlin&page=0&city=&pageSize=200";


    public ReweGeoLocationsResponse getReweGeoLocations() {

        ReweGeoLocationsResponse reweGeoLocationsResponse = null;
        try {
            HttpURLConnection con = getHttpURLConnection();
            StringBuffer content = getStringBuffer(con);
            con.disconnect();
            reweGeoLocationsResponse = getReweGeoLocationsResponse(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

            return reweGeoLocationsResponse;


    }

    private HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL(REWE_GEO_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return con;
    }

    private StringBuffer getStringBuffer(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        return content;
    }

    private ReweGeoLocationsResponse getReweGeoLocationsResponse(StringBuffer content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(String.valueOf(content), ReweGeoLocationsResponse.class);
    }
}
