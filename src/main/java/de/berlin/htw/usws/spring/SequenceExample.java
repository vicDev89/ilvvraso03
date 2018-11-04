package de.berlin.htw.usws.spring;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SequenceExample {

    // Alle %10 Sekunden
    @Scheduled(cron="*/10 * * * * *")
    public void scheduledExample() {
        System.out.println("Hallo Spring!");
    }

}
