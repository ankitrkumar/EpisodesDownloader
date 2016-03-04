package com.kumarankit.episodedownloader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Ankit on 3/4/2016.
 * ShowNameManager:     Stores show names in JSON file
 */
public class ShowNameManager {

    private String partFileName = "./showIndex";
    private String extJSON = ".json";

    private JSONObject showNameJSON = new JSONObject();

    public void put(String showName, String showURL)
    {
        showNameJSON.put(showName, showURL);
    }

    public String get (String showName)
    {
        try {
            JSONObject allShowsByLetter = readFromFile(constructFileName(showName));
            System.out.println(showName);
            return allShowsByLetter.get(showName).toString();
        }
        catch (Exception e)
        {
            System.out.println("Looks like you are running this utility for the first time" +
                    "\n" + "Please run the command java -jar EpisodesDownloader -update");
        }
        return null;
    }

    public void writeToFile(String fileName)
    {
        try (FileWriter file = new FileWriter(constructFileName(fileName))) {
            file.write(showNameJSON.toJSONString());
        } catch (IOException e) {
            System.out.println("Index creation error, download failed... Retrying..");
        }
    }

    private JSONObject readFromFile(String fileName)
    {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try {
            Object obj = parser.parse(new FileReader(fileName));
            return (JSONObject) obj;
        }
            catch (Exception e)
            {
                System.out.println("Looks like you are running this utility for the first time" +
                        "\n" + "Please run the command java -jar EpisodesDownloader -update");
            }
        return jsonObject;
    }
    private String constructFileName(String filePart)
    {
        if(Character.isLetter(filePart.charAt(0)))
            return partFileName + filePart.charAt(0) + extJSON;
        else
            return partFileName + "09" + extJSON;
    }
}
