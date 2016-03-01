package com.kumarankit.episodedownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        //Sample for study : http://thewatchseries.to/episode/modern_family_s7_e14.html

        String seriesName = "", episodeNumber = "";
        if(args[1].equals("-l"))                     //ED "Modern Family" -l  #download the latest episode of this series
        {
            constructLatestEpURL();
        }
        else                                         ////Input given as ED "Modern Family" s1-e14
        {
            seriesName = args[0].replace(" ","_");
            //parse the season number and episode number
            episodeNumber = parseEpisodeNumber(args[1]);
        }

        //Parse the series name


        //create the url
        String requestURL= Constants.WEBPAGE + "/" + Constants.EPISODE + "/" + seriesName + "_" + episodeNumber + Constants.HTML.toLowerCase();
        System.out.println(requestURL);

        String encodedLink = makeRequest(requestURL);
        System.out.println(encodedLink);
        String downloadLink = decodeBase64(encodedLink);
        System.out.println(downloadLink);
        try {
            DownloadVideo(downloadLink);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void constructLatestEpURL() {
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
                break;
            }
        }
        return downloadLink;
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static void DownloadVideo(String url) throws IOException {

        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "youtube-dl "+ url);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
    }
}
