package com.kumarankit.episodedownloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        for (String s: args)
        {
            System.out.println(s);
        }
        //Parse the series name
        //parse the season number and episode number
        //create the url
        String seriesName = args[0].replace(" ","_");       //Input given as ED "Modern Family" -s s1e14
                                                            //               ED "Modern Family" -l  #download the latest episode of this series
        String season = args[1];
        StringBuilder requestURL = new StringBuilder();
        //requestURL= Constants.WEBPAGE;
        makeRequest(Constants.WEBPAGE);
    }

    public static void makeRequest(String constructedURL)
    {
        StringBuilder HTMLResult = new StringBuilder();
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(constructedURL).openStream(), "UTF-8"))) {
                for (String line; (line = reader.readLine()) != null;) {
                    HTMLResult.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(HTMLResult.toString());
    }
}
