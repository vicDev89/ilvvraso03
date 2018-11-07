package de.berlin.htw.usws.scheduler;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;


@Startup
@Singleton
public class ScheduleService {

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void initialisiereTasks() {

        final TimerConfig timerConfig = new TimerConfig("Recipe Crawling Task", false);

        // Beispiel mit Ausführungen jede 10 Sekunden
        String taskCron = "10 * * * * * *";
        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.second("10");
        scheduleExpression.minute("*");
        scheduleExpression.hour("*");
        scheduleExpression.dayOfMonth("*");
        scheduleExpression.dayOfWeek("*");
        scheduleExpression.month("*");
        scheduleExpression.year("*");

        final Timer timer = this.timerService.createCalendarTimer(scheduleExpression, timerConfig);

    }

    @Timeout
    public void timeoutHandler(final Timer timer) {

        System.out.println("##### " + timer.getInfo() + " #####");
        System.out.println("#### Timer struck ####");
        // When time is out, start crawler
        //(new CrawlerService()).start();
    }

}
