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

    public static String BASEURL = "http://thewatchseries.to";
    public static String SERIES = "serie";
    public static String EPISODE = "episode";
    public static String HTML = ".html";

    @Override
    public List<String> GetRequestUrlsFromShow(String showName, String seasonEpisode) {
        String targetUrl = BASEURL+ "/"
                + EPISODE + "/"
                + constructEpURL(showName, seasonEpisode)
                + HTML.toLowerCase();
        Elements elements = makeRequest(targetUrl);

        List<String> encodedUrls = getEncodedVideoUrls(elements);
        List<String> downloadUrls = getDecodedVideoUrls(encodedUrls);
        return downloadUrls;

    }

    @Override
    public List<String> GetLatestEpisodeUrls(String showName) {
        String targetUrl = BASEURL + "/" + SERIES + "/" + parseSpaces(showName);
        Elements elements = makeRequest(targetUrl);

        String showUrl = getLatestEpisodeLink(elements);
        elements = makeRequest(showUrl);
        List<String> encodedUrls = getEncodedVideoUrls(elements);
        List<String> downloadUrls = getDecodedVideoUrls(encodedUrls);
        return downloadUrls;
        //return getLatestEpisodeLink(makeRequest(targetUrl));
    }

    private String constructLatestEpURL(String[] args) {
        String pageURL = BASEURL + "/" + SERIES + "/" + parseSpaces(args[0]);
        return getLatestEpisodeLink(makeRequest(pageURL));
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
        return parseSpaces(showName) + "_" + parseEpisodeNumber(seasonEpisode);
    }

    public String parseEpisodeNumber(String str)
    {
        return str.replace("e","_e");
    }

    private String parseSpaces(String arg) {
        return arg.replace(" ","_");
    }

    private String getLatestEpisodeLink(Elements elements) {
        for (Element e : elements) {
            if(e.text().contains("links)") && e.attr("abs:href").contains(BASEURL + "/" + EPISODE))
            {
                return e.attr("abs:href");
            }
        }
        return "";
    }

    private Elements makeRequest(String constructedURL)
    {
        Document doc = null;
        try {
            doc = Jsoup.connect(constructedURL).get();
        } catch (IOException e) {
            System.out.println("The requested show isn't available.");
        }
        return doc != null ? doc.select("a[href]") : null;
    }
}
