package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Product;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor del Campo
 */
public class EdekaCrawler {

    private final static String homePageEdekaShop = "https://www.edeka24.de/";


    public  static List<Product> getAllProducts () {

        String pathToChromeDriver = "src//main//resources//ChromeDriver//chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);

        ChromeDriver driver = new ChromeDriver();

        driver.get(homePageEdekaShop);

        List<Product> edekaProducts = new ArrayList<Product>();

        WebElement element =  driver.findElementByXPath("/html/body/header/div/div[1]/div[5]/a/span");
        element.click();

        System.out.println("Blabla");

        driver.close();


        return edekaProducts;
    }

}
