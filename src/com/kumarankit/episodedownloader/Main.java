package com.kumarankit.episodedownloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;


/*
* Main.java: Entry point to the EpisodesDownloader Utility
* */
public class Main {
    boolean download_success = false;
    int failed_count = 0;

    public static void main(String[] args) {
        EpisodeDownloader epiDown = new EpisodeDownloader();
        epiDown.downLoadVideo(args);
    }
}
