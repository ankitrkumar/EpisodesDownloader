package com.kumarankit.episodedownloader.Logger;

/**
 * Created by Ankit on 3/4/2016.
 *
 * ILogger-     Interface for Logger files
 */
public interface ILogger {

    void Debug(String msg);
    void Error(String msg);
    void Warning(String msg);
    void Exception(String msg);
}
