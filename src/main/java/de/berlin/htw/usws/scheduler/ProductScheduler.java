package de.berlin.htw.usws.scheduler;

import de.berlin.htw.usws.services.ProductService;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;

@Scheduled(cronExpression = "15 * * * * ?")
public class ProductScheduler implements org.quartz.Job {


    @Inject
    private ProductService productService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("#### Timer struck for ProductScheduler ####");
    }
}
