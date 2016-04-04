# Episodes Downloader.

[![Under Development](https://img.shields.io/badge/under-development-orange.svg)](https://github.com/cez-aug/github-project-boilerplate) [![Public Domain](https://img.shields.io/badge/public-domain-lightgrey.svg)](https://creativecommons.org/publicdomain/zero/1.0/)

#### Work in Progress.
#### Experimental Java program to download an episode of any tv series listed on thewatchseries.to

Download for windows [youtube-dl](https://yt-dl.org/downloads/2016.03.01/youtube-dl.exe) and save it in the same folder as the EpisodeDownloader.jar folder. 

To run the program,

This command needs tobe run the first time to index all the avaialbel shows.

```java -jar path_to_jar_file/EpisodesDownloader.jar -update```

To download the latest episode,

```java -jar path_to_jar_file/EpisodesDownloader.jar "series name" -l```

-l is the argument to download the latest episode

To download a specific episode

```java -jar path_to_jar_file/EpisodesDownloader.jar "series name" s1e14```

s(number) is the season number e(number) is the episode number.