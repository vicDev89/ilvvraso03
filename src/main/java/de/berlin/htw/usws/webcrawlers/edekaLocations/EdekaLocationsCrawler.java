package de.berlin.htw.usws.webcrawlers.edekaLocations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ejb.Stateless;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Stateless
public class EdekaLocationsCrawler {

    private static final String EDEKA_GEO_URL = "https://www.edeka.de/search.xml";

    private static final String POST_DATA = "indent=off&hl=false&rows=400&q=((indexName%3Ab2cMarktDBIndex++AND+kanalKuerzel_tlcm%3Aedeka+AND+((freigabeVonDatum_longField_l%3A%5B0+TO+1546556399999%5D+AND+freigabeBisDatum_longField_l%3A%5B1546470000000+TO+*%5D)+AND+NOT+(datumAppHiddenVon_longField_l%3A%5B0+TO+1546556399999%5D+AND+datumAppHiddenBis_longField_l%3A%5B1546470000000+TO+*%5D))+AND+geoLat_doubleField_d%3A%5B52.486319776845505+TO+52.55369342315447%5D+AND+geoLng_doubleField_d%3A%5B13.349592139893844+TO+13.460315860106107%5D)+OR+(indexName%3Ab2cMarktDBIndex++AND+kanalKuerzel_tlcm%3Aedeka+AND+((freigabeVonDatum_longField_l%3A%5B0+TO+1546556399999%5D+AND+freigabeBisDatum_longField_l%3A%5B1546470000000+TO+*%5D)+AND+NOT+(datumAppHiddenVon_longField_l%3A%5B0+TO+1546556399999%5D+AND+datumAppHiddenBis_longField_l%3A%5B1546470000000+TO+*%5D))+AND+geoLat_doubleField_d%3A%5B52.25051201476413+TO+52.78950118523584%5D+AND+geoLng_doubleField_d%3A%5B12.962056384869584+TO+13.847851615130367%5D))&fl=handzettelSonderlUrl_tlcm%2ChandzettelSonderName_tlcm%2ChandzettelUrl_tlc%2CmarktID_tlc%2Cplz_tlc%2Cort_tlc%2Cstrasse_tlc%2Cname_tlc%2CgeoLat_doubleField_d%2CgeoLng_doubleField_d%2Ctelefon_tlc%2Cfax_tlc%2Cservices_tlc%2Coeffnungszeiten_tlc%2CknzUseUrlHomepage_tlc%2CurlHomepage_tlc%2CurlExtern_tlc%2CmarktTypName_tlc%2CmapsBildURL_tlc%2CvertriebsschieneName_tlc%2CvertriebsschienenTypeKuerzel_tlc%2CvertriebsschieneKey_tlc%2CfreigabeVonDatum_longField_l%2CfreigabeBisDatum_longField_l%2CdatumAppHiddenVon_longField_l%2CdatumAppHiddenBis_longField_l%2CoeffnungszeitenZusatz_tlc%2CknzTz_tlc%2CkaufmannIName_tlc%2CkaufmannIStrasse_tlc%2CkaufmannIPlz_tlc%2CkaufmannIOrt_tlc%2CsonderoeffnungszeitJahr_tlcm%2CsonderoeffnungszeitMonat_tlcm%2CsonderoeffnungszeitTag_tlcm%2CsonderoeffnungszeitUhrzeitBis_tlcm%2CsonderoeffnungszeitUhrzeitVon_tlcm%2CregionName_tlc";

    private byte[] postData = POST_DATA.getBytes(StandardCharsets.UTF_8);

    private int postDataLength = postData.length;

    public EdekaGeoLocationsResponse getEdekaGeoLocations() {

        EdekaGeoLocationsResponse edekaGeoLocationsResponse = null;
        try {
            HttpURLConnection con = getHttpURLConnection();
            StringBuffer content = getStringBuffer(con);
            con.disconnect();
            edekaGeoLocationsResponse = getEdekaGeoLocationsResponse(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return edekaGeoLocationsResponse;


    }

    private HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL(EDEKA_GEO_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput( true );
        con.setInstanceFollowRedirects( false );
        con.setRequestMethod( "POST" );
        con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty( "charset", "utf-8");
        con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        con.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
            wr.write( postData );
        }
        return con;
    }

    private StringBuffer getStringBuffer(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        return content;
    }

    private EdekaGeoLocationsResponse getEdekaGeoLocationsResponse(StringBuffer content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(String.valueOf(content), EdekaGeoLocationsResponse.class);
    }
}
