package org.example;


import org.example.Spotify_API.Downloader.ImageDownloader;
import org.example.Spotify_API.Models.MetaData;
import org.example.Spotify_API.PlaylistExtractor;
import org.example.Spotify_API.SpotifyApiClient;
import org.example.Spotify_API.TrackExtractor;
import org.example.YT.YoutubeSearch;
import org.example.YT_downloader.Downloader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class App {
    ///home/mohanasundharam/Downloads
//https://open.spotify.com/playlist/7DykIDeBmEKQ5hsiYeCeE0?si=f11ba8e0051a464b
    public static void main(String[] args) throws IOException, CannotWriteException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter a Playlist Link or Song Link: ");
        Map<String, String> givenLinkData = getGivenLinkId(input.nextLine());
        System.out.println("Enter a Directory Path: ");
        String directory = input.nextLine();
        var temp = new File(directory + "/" + "Images");
        temp.mkdirs();
        System.out.println("Enter a Directory Name:");
        directory = directory + "/" + input.nextLine();
        new File(directory).mkdirs();
        collectSongsData(givenLinkData.get("Id"), givenLinkData.get("Type"), directory,temp);


    }

    private static void collectSongsData(String id, String type, String directory,File imgPath) throws IOException {
        String accessToken = new SpotifyApiClient().getAccessToken();


        if (type.equals("playlist")) {
            List<MetaData> songs = new PlaylistExtractor().SongsList(accessToken, id);
            songs.forEach(song -> {
                if (!Objects.equals(song.getSongName(), "none")) {
                    YoutubeSearch search = new YoutubeSearch(song);
                    search.setDownloadId();

                    try {
                        new Downloader(song, new File(directory),imgPath).downloadAudio();
                    } catch (IOException | CannotWriteException | CannotReadException | TagException |
                             InvalidAudioFrameException | ReadOnlyFileException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {


        }
    }

    private static HashMap<String, String> getGivenLinkId(String s) {
        if (isPlayList(s)) {
            return new HashMap<String, String>() {{
                put("Id", s.substring(s.indexOf("playlist/") + 9, s.indexOf("?si=")));
                put("Type", "playlist");
            }};
        } else {
            return new HashMap<String, String>() {{
                put("Id", s.substring(s.indexOf("track/") + 6, s.indexOf("?si=")));
                put("Type", "song");
            }};
        }
    }

    private static boolean isPlayList(String s) {
        return s.contains("playlist");

    }

}