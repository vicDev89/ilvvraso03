package de.berlin.htw.usws.scheduler;

import de.berlin.htw.usws.services.CrawlerService;
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
        String taskCron = "********";
        final TimerConfig timerConfig = new TimerConfig("Recipe Task", false);
        final Timer timer = this.timerService.createCalendarTimer(Cron.create(taskCron).asScheduleExpression(), timerConfig);
        log.debug("Timer eingestellt {}", timer.getInfo());
        log.debug("Naechste Ausfuehrung {}", timer.getNextTimeout());
    }

    @Timeout
    public void timeoutHandler(final Timer timer) {

        // When time is out, start crawler
        (new CrawlerService()).start();
    }

}
