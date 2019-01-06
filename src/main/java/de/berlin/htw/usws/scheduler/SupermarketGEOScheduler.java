package de.berlin.htw.usws.scheduler;

import com.google.common.base.Stopwatch;
import de.berlin.htw.usws.model.Protokoll;
import de.berlin.htw.usws.model.SupermarketGEO;
import de.berlin.htw.usws.repositories.ProtokollRepository;
import de.berlin.htw.usws.repositories.SupermarketGEORepository;
import de.berlin.htw.usws.webcrawlers.edekaLocations.EdekaGeoLocationsResponse;
import de.berlin.htw.usws.webcrawlers.edekaLocations.EdekaLocationsCrawler;
import de.berlin.htw.usws.webcrawlers.edekaLocations.EdekaMarketGeoLocation;
import de.berlin.htw.usws.webcrawlers.reweLocations.ReweGeoLocationsResponse;
import de.berlin.htw.usws.webcrawlers.reweLocations.ReweLocationsCrawler;
import de.berlin.htw.usws.webcrawlers.reweLocations.ReweMarketGeoLocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

// Einmal die Woche am Samstag um 6Uhr morgens --> 0 0 6 ? * SAT *
@Scheduled(cronExpression = "0 0 6 ? * SAT *")
@Slf4j
public class SupermarketGEOScheduler implements org.quartz.Job {

    @Inject
    private SupermarketGEORepository supermarketGEORepository;

    @Inject
    private EdekaLocationsCrawler edekaLocationsCrawler;

    @Inject
    private ReweLocationsCrawler reweLocationsCrawler;

    @Inject
    private ProtokollRepository protokollRepository;

    private int numberUpdates = 0;

    private int numberNews = 0;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("#### SUPERMARKETGEO SCHEDULER started at: " + LocalDateTime.now() + " ####");


        Stopwatch swSupermarketGEO = Stopwatch.createStarted();
//        Stopwatch swSupermarketGEO = (new Stopwatch()).start();

        // EDEKA
        EdekaGeoLocationsResponse edekaGeoLocationsResponse = this.edekaLocationsCrawler.getEdekaGeoLocations();
        for (EdekaMarketGeoLocation market : edekaGeoLocationsResponse.getResponse().getDocs()) {
            SupermarketGEO supermarketGEO = market.convertEdekaMarketGeoLocationToSupermarketGeo();
            persistNewOrUpdate(supermarketGEO);
        }

        // REWE
        ReweGeoLocationsResponse reweGeoLocationsResponse = this.reweLocationsCrawler.getReweGeoLocations();
        for (ReweMarketGeoLocation market : reweGeoLocationsResponse.getMarkets()) {
            SupermarketGEO supermarketGEO = market.convertReweMarketGeoLocationToSupermarketGeo();
            persistNewOrUpdate(supermarketGEO);
        }

        log.info("#### All SupermarketGEO scrapped and persisted. Duration: ####" + swSupermarketGEO.elapsed(TimeUnit.SECONDS) + " seconds.");
//        log.info("#### All SupermarketGEO scrapped and persisted. Duration: ####" + swSupermarketGEO.elapsedTime(TimeUnit.SECONDS) + " seconds.");

        // Create Protokoll
        Protokoll protokoll = new Protokoll();
        protokoll.setErzeuger("SupermarketGEO Scheduler");
        protokoll.setNewSupermarketGEO(numberNews);
        protokoll.setUpdateSupermarketGEO(numberUpdates);
        this.protokollRepository.save(protokoll);

    }

    private void persistNewOrUpdate(SupermarketGEO supermarketGEO) {
        if (this.supermarketGEORepository.findSupermarketGEOExakt(supermarketGEO).size() == 0) {
            // Wenn es exakt nicht gefunden wird, wird nach markt-ID und Supermarket gesucht
            SupermarketGEO supermarketGEOfromDB = this.supermarketGEORepository.findByMarktIDAndSupermarket(supermarketGEO.getMarketID(), supermarketGEO.getSupermarket());
            // Wenn ein SupermarketGEO gefunden wurde, werden die Daten aktualisiert
            if (supermarketGEOfromDB != null) {
                supermarketGEOfromDB.setSupermarketName(supermarketGEO.getSupermarketName());
                supermarketGEOfromDB.setStreet(supermarketGEO.getStreet());
                supermarketGEOfromDB.setHousenumber(supermarketGEO.getHousenumber());
                supermarketGEOfromDB.setZip(supermarketGEO.getZip());
                supermarketGEOfromDB.setPhonenumber(supermarketGEO.getPhonenumber());
                supermarketGEOfromDB.setCity(supermarketGEO.getCity());
                supermarketGEOfromDB.setLat(supermarketGEO.getLat());
                supermarketGEOfromDB.setLng(supermarketGEO.getLng());
                this.supermarketGEORepository.save(supermarketGEOfromDB);
                numberUpdates++;
            } else {
                // Sonst wird der neue SupermarketGEO persistiert
                this.supermarketGEORepository.save(supermarketGEO);
                numberNews++;
            }
        }
    }

}
