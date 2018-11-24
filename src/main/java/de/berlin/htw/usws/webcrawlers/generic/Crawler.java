package de.berlin.htw.usws.webcrawlers.generic;

import lombok.Getter;
import lombok.Setter;
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

    // TODO: [Lucas] Vorschlag: Definieren finaler CSS-Queries in dieser Klasse. (z.B. "a", "href", "li", h1 ...). So könnte man sich das Deklarieren solcher Queries in den Subklassen sparen (siehe UnknownIdsCrawler)

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
        return Jsoup.connect(url).maxBodySize(0).get();
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
