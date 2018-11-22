package de.berlin.htw.usws.scheduler;

import de.berlin.htw.usws.services.ChefkochCrawlerService;
import de.berlin.htw.usws.services.HellofreshCrawlerService;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;

@Scheduled(cronExpression = "10 * * * * ?")
public class RecipeScheduler implements org.quartz.Job{

    @Inject
    private ChefkochCrawlerService chefkochCrawlerService;

    @Inject
    private HellofreshCrawlerService hellofreshCrawlerService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("#### Timer struck for RecipeScheduler ####");
      //  this.chefkochCrawlerService.start();

      //  this.hellofreshCrawlerService.start();
    }
}
