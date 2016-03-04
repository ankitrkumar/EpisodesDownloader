package com.kumarankit.episodedownloader;

/*
* Main.java: Entry point to the EpisodesDownloader Utility
* */
public class Main {

    public static void main(String[] args) {
        EpisodeDownloader epiDown = new EpisodeDownloader();
        epiDown.downoadVideo(args);
    }
}
