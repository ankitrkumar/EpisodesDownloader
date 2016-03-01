package com.kumarankit.episodedownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        //Sample for study : http://thewatchseries.to/episode/modern_family_s7_e14.html

        //Parse the series name
        String seriesName = args[0].replace(" ","_");       //Input given as ED "Modern Family" s1-e14

        //parse the season number and episode number                                                    //               ED "Modern Family" -l  #download the latest episode of this series
        String episodeNumber = parseEpisodeNumber(args[1]);

        //create the url
        String requestURL= Constants.WEBPAGE + "/" + Constants.EPISODE + "/" + seriesName + "_" + episodeNumber + Constants.HTML.toLowerCase();
        System.out.println(requestURL);

        String downloadLink = makeRequest(requestURL);
        System.out.println(decodeBase64(downloadLink));
    }

    private static String decodeBase64(String encodedString)
    {
        return new String(Base64.getDecoder().decode(encodedString));
    }

    public static String parseEpisodeNumber(String str)
    {
        return str.replace("e","_e");
    }

    private static String makeRequest(String constructedURL)
    {
        Document doc = null;
        try {
            doc = Jsoup.connect(constructedURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements links = doc.select("a[href]");
        return parseLinks(links);
    }

    private static String parseLinks(Elements e)
    {
        String downloadLink = "";
        print("\nLinks: (%d)", e.size());

        for (Element link : e) {
            if(link.attr("abs:href").contains("cale.html?r"))
            {
                downloadLink = link.attr("href").replace("?","").substring(12);
                System.out.println(downloadLink);
                break;
            }
        }
        return downloadLink;
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width)
    {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
