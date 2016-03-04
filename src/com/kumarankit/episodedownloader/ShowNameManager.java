package com.kumarankit.episodedownloader;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Ankit on 3/4/2016.
 * ShowNameManager:     Stores show names in JSON file
 */
public class ShowNameManager {

    private JSONObject showNameJSON = new JSONObject();

    public void put(String showName, String showURL)
    {
        showNameJSON.put(showName, showURL);
    }

    public String get (String showName)
    {
        System.out.println(showNameJSON.get(showName).toString());
        return showNameJSON.get(showName).toString();
    }
    public void writeToFile()
    {
        try (FileWriter file = new FileWriter("./showIndex.json")) {
            file.append(showNameJSON.toJSONString());
        } catch (IOException e) {
            System.out.println("Index creation error, download failed.");
        }
    }
}
