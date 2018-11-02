package de.berlin.htw.usws.scheduler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ejb.ScheduleExpression;

@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Cron {

    private String sekunde;
    private String minute;
    private String stunde;
    private String tagMonat;
    private String tagWoche;
    private String monat;
    private String jahr;


    public static Cron create(final String cronString) {
        final String[] elements = cronString.split(" ");
        Cron c = new Cron();
        c.setSekunde(elements[0]);
        c.setMinute(elements[1]);
        c.setStunde(elements[2]);
        c.setTagMonat(elements[3]);
        c.setTagWoche(elements[4]);
        c.setMonat(elements[5]);
        c.setJahr(elements[6]);
        return c;
    }

    // Bei CronTab ist es möglich:
    // *   --> Ausführung immer (zu jeder...)
    // */10 --> Ausführung aller n
    //  n,x,y --> Ausführung um/am n, x und y
    //  die SchedulaExpression akzeptiert alle dieser Formate
    public ScheduleExpression asScheduleExpression() {
        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.second(this.getSekunde());
        scheduleExpression.minute(this.getMinute());
        scheduleExpression.hour(this.getStunde());
        scheduleExpression.dayOfMonth(this.getTagMonat());
        scheduleExpression.dayOfWeek(this.getTagWoche());
        scheduleExpression.month(this.getMonat());
        scheduleExpression.year(this.getJahr());
        return scheduleExpression;
    }
}
