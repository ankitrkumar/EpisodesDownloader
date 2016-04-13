package com.kumarankit.episodedownloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Akhil Panchal on 3/1/2016.
 * EpisodeDownloader.java:  Responsible for downloading
 *                          the requested episode from the URLs given by a Video Source
 */
public class EpisodeDownloader {

    private boolean download_success;
    private int failCount;
    private VideoSource videoSource;

    public EpisodeDownloader() {
        videoSource = new WatchSeries();
    }

    public void downloadVideo(String[] args) {
        try{
            List<String> targetURLs = parseCommandLine(args);
            while(!download_success) {
                try {
                    if(targetURLs !=null && targetURLs.size() > 0)
                    DownloadVideo(targetURLs.get(failCount));
                    else
                        break;
                } catch (Exception e) {
                    System.out.println("Download attempt failed from link " + failCount + ". Retrying...");
                }
            }
            if(!download_success)
                System.out.println("\nNo links found for this show.");
        }catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("\nError in fetching and downloading file. Verify if the episode exists.");

            System.out.println("Input command arguments incorrect" +
                    "\n\n Use following format to download an episode when you know the exact episode::\n" +
                    "java -jar EpisodesDownloader \"Modern Family\" s3e12\n\n" +
                    "To get the latest episode, use the following format::\n" +
                    "java -jar EpisodesDownloader \"Modern Family\" -l\n");
        }

    }

    private List<String> parseCommandLine(String[] args)
    {
        if (args.length == 2 && args[1].equals("-l"))                     //ED "Modern Family" -l  #download the latest episode of this series
        {
            return videoSource.GetLatestEpisodeUrls(args[0]);
        }
        else if (args.length == 2 && !args[1].equals("-l"))                                         ////Input given as ED "Modern Family" s1-e14
        {
            return videoSource.GetRequestUrlsFromShow(args[0], args[1]);
        }
        else if (args.length == 1 && args[0].equals("-update"))                                         ////Input given as ED "Modern Family" s1-e14
        {
            videoSource.GetAllAvailableShows();
            download_success = true;
            System.out.println("Website index updated! Proceed to download.");
        }
        else {
            System.out.println("Input command arguments incorrect" +
                    "\n\n Use following format to download an episode when you know the exact episode::\n" +
                    "java -jar EpisodesDownloader \"Modern Family\" s3e12\n\n" +
                    "To get the latest episode, use the following format::\n" +
                    "java -jar EpisodesDownloader \"Modern Family\" -l\n");
        }
        return null;
    }
    private void DownloadVideo(String url) throws IOException
    {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "youtube-dl "+ url);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        System.out.println("\n\n");
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            System.out.print(line + "\r");
            if(line.contains("ERROR"))
            {
                download_success = false;
                failCount++;
            }
            else if (line.contains("100%")){
                download_success = true;
            }
        }
    }
}
