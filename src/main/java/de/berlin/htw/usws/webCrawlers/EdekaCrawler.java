package de.berlin.htw.usws.webCrawlers;

import de.berlin.htw.usws.model.Product;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor del Campo
 */
public class EdekaCrawler {

    private final static String homePageEdekaShop = "https://www.edeka24.de/";

    private static ChromeDriver driver;

    private static  List<Product> edekaProducts = new ArrayList<Product>();

    private static List<String> lebensmittelUrls = new ArrayList<String>();

    private static List<String> getraenkeUrls = new ArrayList<String>();


    public static void initChromeDriver() {
        String pathToChromeDriver = "src//main//resources//ChromeDriver//chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);

        driver = new ChromeDriver();
        // To open the Chrome-Browser in its full size (because of feature of edeka website)
        driver.manage().window().maximize();
    }


    public  static List<Product> getAllProducts () {

        initChromeDriver();

        driver.get(homePageEdekaShop);

        // "*" get direct children of current node
        List<WebElement> upperCategories =  driver.findElementsByXPath("/html/body/header/div/div[2]/div/nav/ul/*");

        for(WebElement upperCategory : upperCategories) {
            String upperCategoryText = upperCategory.getText();
            if(upperCategoryText.equals("Lebensmittel")) {
                upperCategory.click();
                List <WebElement> lebensmittelCategories = upperCategory.findElements(By.xpath("/html/body/header/div/div[2]/div/nav/ul/li[1]/ul/li"));
                for(WebElement lebensmittelCategory : lebensmittelCategories) {
                    lebensmittelUrls.add(homePageEdekaShop + upperCategoryText + "/" + formatString(lebensmittelCategory.findElement(By.tagName("a")).getText()));
                }
            }
            if(upperCategory.getText().equals("Getränke")) {
                upperCategory.click();
                List <WebElement> getraenkeCategories = driver.findElementsByXPath("/html/body/header/div/div[2]/div/nav/ul/li[2]/ul/li");
                for(WebElement getraenkeCategory : getraenkeCategories) {
                    getraenkeUrls.add(homePageEdekaShop + formatString(upperCategoryText) + "/" + formatString(getraenkeCategory.findElement(By.tagName("a")).getText()));
                }
            }
        }

        beginProductScraping(lebensmittelUrls);
        beginProductScraping(getraenkeUrls);

        driver.close();

        return edekaProducts;
    }

    public static List<Product> beginProductScraping(List<String> listeUrls) {

        for(String url : listeUrls) {
            System.out.println(url);
            driver.get(url);
        }

        return edekaProducts;

    }

    private static String formatString(String input) {
        String newString = input.replace("\u00fc", "ue")
                .replace("\u00f6", "oe")
                .replace("\u00e4", "ae")
                .replace("\u00df", "ss")
                .replaceAll("\u00dc(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "Ue")
                .replaceAll("\u00d6(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "Oe")
                .replaceAll("\u00c4(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "Ae")
                .replace("\u00dc", "UE")
                .replace("\u00d6", "OE")
                .replace("\u00c4", "AE")
                .replace("\u00df", "ss")
                .replace(" ", "-")
                .replace("-/", "-")
                .replace("/", "-");
        if(newString.equals("Weitere-Konserven")) {
            newString = "Konserven";
        }
        return newString;
    }


}
