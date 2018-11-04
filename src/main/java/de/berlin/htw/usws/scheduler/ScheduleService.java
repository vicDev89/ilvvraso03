package de.berlin.htw.usws.scheduler;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;


/**
 * Service für das Management und die Zeitablaufsteuerung von Hintergrundjobs.
 */
@Startup
@Singleton
@Slf4j
public class ScheduleService {

    @Resource
    private TimerService timerService;

    /**
     * Initialisiert die zeitgesteuerten Tasks
     */
    @PostConstruct
    public void initialisiereTasks() {

        // TODO create cron for Crawler-Service
        // Beispiel mit Ausführungen in jeder Sekunde
        String taskCron = "* * * * * * *";
        final TimerConfig timerConfig = new TimerConfig("Recipe Crawling Task", false);
        final Timer timer = this.timerService.createCalendarTimer(Cron.create(taskCron).asScheduleExpression(), timerConfig);
        log.debug("Timer eingestellt {}", timer.getInfo());
        log.debug("Naechste Ausfuehrung {}", timer.getNextTimeout());


    }

    @Timeout
    public void timeoutHandler(final Timer timer) {

        System.out.println("##### " + timer.getInfo() + " #####");
        System.out.println("#### Timer struck ####");
        // When time is out, start crawler
        //(new CrawlerService()).start();
    }

}
