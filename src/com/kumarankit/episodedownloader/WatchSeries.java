package com.kumarankit.episodedownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by Akhil Panchal on 3/2/2016.
 * WatchSeries.java:    A video source that finds download urls from
 *                      www.watchseries.to
 */
public class WatchSeries implements VideoSource {

    static String BASEURL = "http://thewatchseries.to";
    static String SERIES = "serie";
    static String EPISODE = "episode";
    static String HTML = ".html";
    static String LETTER = "letters";
    ShowNameManager showNameManager = new ShowNameManager();

    @Override
    public List<String> GetRequestUrlsFromShow(String showName, String seasonEpisode)
    {
        String targetUrl = null;
        try {
            targetUrl = BASEURL + "/"
                    + EPISODE + "/"
                    + constructEpURL(showNameManager.get(showName.toUpperCase()), seasonEpisode)
                    + HTML.toLowerCase();
        }
        catch (Exception e)
        {
            System.out.println("Looks like you are running this utility for the first time" +
                    "\n" + "Please run the command java -jar EpisodesDownloader -update");
        }
        return createUrlList(targetUrl);
    }

    @Override
    public List<String> GetLatestEpisodeUrls(String showName)
    {
        String targetUrl = null;
        try {
         targetUrl = BASEURL + "/" + SERIES + "/" + showNameManager.get(showName.toUpperCase());
        }
        catch (Exception e)
        {
            System.out.println("Looks like you are running this utility for the first time" +
                    "\n" + "Please run the command java -jar EpisodesDownloader -update");
        }
        return createUrlList(targetUrl);
    }

    private List<String> createUrlList(String targetURL)
    {
        List<String> encodedUrls = null, downloadUrls = null;
        Elements elements = null;
        String showUrl = null;
        if(targetURL != null) {
            elements = makeRequest(targetURL);
        }
        if(elements != null) {
            showUrl = getLatestEpisodeLink(elements);
        }
        if(showUrl != null) {
            elements = makeRequest(showUrl);
        }
        if(elements != null) {
            encodedUrls= getEncodedVideoUrls(elements);
        }
        if(encodedUrls != null) {
            downloadUrls = getDecodedVideoUrls(encodedUrls);
        }
        return downloadUrls;
    }
    @Override
    public void GetAllAvailableShows()
    {
        //TODO: Reconsider this implementation
        try {
            String[] pages = {"09","A","B","C","D","E","F","G","H","I","J","K","L","M","N",
                    "O","P","Q","R","S","T","U","V","W","X","Y","Z"};
            for (String s : pages) {
                showNameManager = new ShowNameManager();
                Elements elements = makeRequest(constructFetchByCharacterURL(s));
                if (elements != null) {
                    for (Element e : elements) {
                        if (criteriaMatchForMap(e)) {
                            showNameManager.put(getKeyFromName(e), getValueFromLink(e));
                        }
                    }
                }
                showNameManager.writeToFile(s);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getValueFromLink(Element e) {
        return e.attr("href").substring(("/" + SERIES + "/").length());
    }

    private String getKeyFromName(Element e) {
        boolean parsable = true;
        String textFromE = e.text().trim();
        try {
            Integer.parseInt(textFromE.substring(textFromE.length()-4));
        }
        catch(Exception n)  //TODO: split into numberformatexcep and outofboundsexcep?
        {
            parsable = false;
        }
        textFromE = textFromE.toUpperCase();
        if(parsable)
        {
            return textFromE.substring(0, textFromE.length()-4);
        }
        else
            return textFromE;
    }

    private boolean criteriaMatchForMap(Element e) {
        return e.attr("abs:href").contains(BASEURL + "/" + SERIES + "/")
                && e.text() != null
                && e.attr("href").length() > ("/" + SERIES + "/").length();
    }

    private List<String> getEncodedVideoUrls(Elements elements)
    {
        List<String> urls = new ArrayList<>();
        for(Element element : elements) {
            if(element.attr("abs:href").contains("cale.html?r")) {
                urls.add(
                        element.attr("href").replace("?", "").substring(12)
                );
            }
        }
        return urls;
    }
    private List<String> getDecodedVideoUrls(List<String> encodedUrls)
    {
        List<String> decodedUrls = new ArrayList<>();
        for(String url : encodedUrls) {
            decodedUrls.add(
                    new String(Base64.getDecoder().decode(url))
            );
        }
        return decodedUrls;
    }

    private String constructEpURL(String showName, String seasonEpisode) {
        //parse the season number and episode number
        return showName + "_" + parseEpisodeNumber(seasonEpisode);
    }

    /*
    Construct URL for fetching all episodes for all alphabets
     */
    private String constructFetchByCharacterURL(String alphabet)
    {
        return BASEURL + "/" + LETTER + "/" + alphabet;
    }

    public String parseEpisodeNumber(String str)
    {
        return str.replace("e","_e");
    }

    private String getLatestEpisodeLink(Elements elements) {
        //// TODO: 3/4/2016 Check for no links returned...i.e. 0 links for episode avalible
        for (Element e : elements) {
            if(e.text().contains("links)") && e.attr("abs:href").contains(BASEURL + "/" + EPISODE))
            {
                if(existsLinks(e))
                    return e.attr("abs:href");
            }
        }
        return null;
    }

    private Elements makeRequest(String constructedURL)
    {
        Document doc = null;
        try {
            doc = Jsoup.connect(constructedURL).userAgent("Mozilla").get();
        } catch (IOException e) {
            System.out.println("The requested show isn't available.");
        }
        return doc != null ? doc.select("a[href]") : null;
    }

    private boolean existsLinks(Element e)
    {
     return !e.text().contains("(0 links)") && e.attr("abs:href").contains(BASEURL + "/" + EPISODE);
    }
}
