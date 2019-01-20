package de.berlin.htw.usws.webcrawlers.generic;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Abstract class all Crawlers extend. Holds basic methods/logic for web scraping.
 *
 * @author Lucas Larisch
 * @since 24.10.2018
 */
@Setter
@Getter
public abstract class Crawler {

    /**
     * Whole URL of a web page to be parsed.
     */
    private String url;

    /**
     * Base URL of the web page to be parsed. Not final, so that it can be set in subclasses.
     */
    private String baseUrl;

    /**
     * Returns a Document loaded by using the set {@link Crawler#url}.
     * The max body size is set to 'unlimited'.
     *
     * @return Document loaded by using the set URL.
     * @throws IOException
     * @author Lucas Larisch
     * @since 24.10.2018
     */
    protected Document getUnlimitedDocument() throws IOException {

        Connection con = Jsoup.connect(url).timeout(120000); // 2 minutes
        Connection.Response resp = con.execute();
        if (resp.statusCode() == 200) {
            return con.maxBodySize(0).get();
        } else {
            System.err.println("Recipe " + url + " could not be reached");
            return null;
        }
    }

    /**
     * Appends a fragment from an URL to {@link Crawler#baseUrl} and sets it
     * as {@link Crawler#url}.
     *
     * @param append String to append to the {@link Crawler#url}.
     * @author Lucas Larisch
     * @since 24.10.2018
     */
    protected void appendToBaseUrl(String append) {
        this.url = baseUrl + append;
    }

}
