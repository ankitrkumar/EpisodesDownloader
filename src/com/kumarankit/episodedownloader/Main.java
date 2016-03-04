package com.kumarankit.episodedownloader;

/**
 * Created by Ankit on 3/1/2016.
 * Main.java: Entry point to the EpisodesDownloader Utility
 */
public class Main {

    public static void main(String[] args) {
        EpisodeDownloader epiDown = new EpisodeDownloader();
        epiDown.downloadVideo(args);
    }
}
