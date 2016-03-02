package com.kumarankit.episodedownloader;


import java.util.List;

/**
 * Created by Akhil on 3/2/2016.
 */
public interface VideoSource {

    /*TODO: Think about weather to take season and episode as 2 different arguments
    *       If so, then change the interface and the implementers of this interface
    * */
    List<String> GetRequestUrlsFromShow(String showName, String seasonEpisode);
    List<String> GetLatestEpisodeUrls(String showName);
    //Elements GetRemainingEpisodesFromShowsFile(String filePath);

}
