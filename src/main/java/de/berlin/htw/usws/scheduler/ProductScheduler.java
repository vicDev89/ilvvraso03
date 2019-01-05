package de.berlin.htw.usws.scheduler;

import com.google.common.base.Stopwatch;
import de.berlin.htw.usws.model.Ingredient;
import de.berlin.htw.usws.model.Product;
import de.berlin.htw.usws.model.Protokoll;
import de.berlin.htw.usws.model.enums.Supermarket;
import de.berlin.htw.usws.repositories.IngredientRepository;
import de.berlin.htw.usws.repositories.ProductRepository;
import de.berlin.htw.usws.repositories.ProtokollRepository;
import de.berlin.htw.usws.webcrawlers.bringmeister.BringmeisterProductAPI;
import de.berlin.htw.usws.webcrawlers.rewe.ReweCrawler;
import lombok.extern.slf4j.Slf4j;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Einmal die Woche am Sonntag um 6Uhr morgens --> 0 0 6 ? * SUN *
@Scheduled(cronExpression = "0 0 6 ? * SUN *")
@Slf4j
public class ProductScheduler implements org.quartz.Job{

    @Inject
    private IngredientRepository ingredientRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private BringmeisterProductAPI bringmeisterProductAPI;

    @Inject
    private ReweCrawler reweCrawler;

    @Inject
    private ProtokollRepository protokollRepository;

    private int numberNewProductsPersisted = 0;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("#### PRODUCT SCHEDULER started at: " + LocalDateTime.now() + " ####");

        Stopwatch swProductScrapperAndPersister = Stopwatch.createStarted();
//        Stopwatch swProductScrapperAndPersister = (new Stopwatch()).start();

        List<Ingredient> allIngredients = this.ingredientRepository.findAllIngredients();

        for (Ingredient ingredient : allIngredients) {
            if(this.productRepository.findByIngredientnameAndSupermarket(ingredient.getName(), Supermarket.EDEKA) == 0) {
                List<Product> productsBringmeister = this.bringmeisterProductAPI.getProducts(ingredient.getName());
                persistProducts(productsBringmeister, ingredient);
            }

            if(this.productRepository.findByIngredientnameAndSupermarket(ingredient.getName(), Supermarket.REWE) == 0) {
                try {
                    List<Product> productsRewe = this.reweCrawler.getProductForIngredientREWE(ingredient.getName());
                    persistProducts(productsRewe, ingredient);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        log.info("#### All products scrapped and persisted. Duration: ####" + swProductScrapperAndPersister.elapsed(TimeUnit.SECONDS) + " seconds.");
//        log.info("#### All products scrapped and persisted. Duration: ####" + swProductScrapperAndPersister.elapsedTime(TimeUnit.SECONDS) + " seconds.");

        // Create Protokoll
        Protokoll protokoll = new Protokoll();
        protokoll.setErzeuger("Product Scheduler");
        protokoll.setNewProductsPersisted(numberNewProductsPersisted);
        this.protokollRepository.save(protokoll);
    }

    private void persistProducts(List<Product> products, Ingredient ingredient) {
        if (products != null) {
            numberNewProductsPersisted+=products.size();
            for (Product product : products) {
                if (product != null && this.productRepository.findByProductnameAndSupermarket(product.getName(), product.getSupermarket()) == null) {
                    product.setIngredient(ingredient);
                   this.productRepository.save(product);
                }
            }
        }
    }
}
