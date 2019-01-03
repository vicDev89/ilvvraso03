package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.webcrawlers.reweLocations.ReweMarketGeoLocation;
import de.berlin.htw.usws.webcrawlers.reweLocations.ReweGeoLocationsResponse;
import de.berlin.htw.usws.webcrawlers.reweLocations.ReweLocationsCrawler;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ReweLocationsCrawlerTest {

    private ReweLocationsCrawler reweLocationsCrawler = new ReweLocationsCrawler();

    @Test
    @Ignore
    public void testCrawlReweGeoLocations() throws IOException {

        ReweGeoLocationsResponse reweGeoLocationsResponse = reweLocationsCrawler.getReweGeoLocations();

        for(ReweMarketGeoLocation market : reweGeoLocationsResponse.getMarkets()) {
            System.out.println(market.toString());
        }

        assertEquals(reweGeoLocationsResponse.getTotal() , reweGeoLocationsResponse.getMarkets().size());
    }
}
