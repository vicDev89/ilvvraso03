package de.berlin.htw.usws.webcrawlers;

import de.berlin.htw.usws.webcrawlers.edekaLocations.EdekaGeoLocationsResponse;
import de.berlin.htw.usws.webcrawlers.edekaLocations.EdekaLocationsCrawler;
import de.berlin.htw.usws.webcrawlers.edekaLocations.EdekaMarketGeoLocation;
import de.berlin.htw.usws.webcrawlers.reweLocations.ReweMarketGeoLocation;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EdekaLocationCrawlerTest {

    private EdekaLocationsCrawler edekaLocationsCrawler = new EdekaLocationsCrawler();

    @Test
    @Ignore
    public void testCrawlReweGeoLocations() throws IOException {

        EdekaGeoLocationsResponse edekaGeoLocationsResponse = edekaLocationsCrawler.getEdekaGeoLocations();

        for(EdekaMarketGeoLocation market : edekaGeoLocationsResponse.getResponse().getDocs()) {
            System.out.println(market.toString());
        }

        assertEquals(edekaGeoLocationsResponse.getResponse().getNumFound() , edekaGeoLocationsResponse.getResponse().getDocs().size());


    }
}
