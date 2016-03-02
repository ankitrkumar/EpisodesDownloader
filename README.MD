#Work in progress.

####Experimental Java program to download an episode of any tv series listed on thewatchseries.to

Download [youtube-dl](https://yt-dl.org/downloads/2016.03.01/youtube-dl.exe) and save it in the same folder as the EpisodeDownloader.jar folder. 

To run the program,

java -jar path_to_jar_file/EpisodesDownloader.jar "series name" -l

-l is the argument to download the latest episode

to download a specific episode

java -jar path_to_jar_file/EpisodesDownloader.jar "series name" s1e14

s(number) is the season number e(number) is the episode number.