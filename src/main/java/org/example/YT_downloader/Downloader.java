package org.example.YT_downloader;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.YoutubeProgressCallback;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.github.kiulian.downloader.model.videos.formats.Format;
import org.example.Spotify_API.Downloader.ImageDownloader;
import org.example.Spotify_API.Downloader.SongDetailsAdder;
import org.example.Spotify_API.Models.MetaData;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Downloader {

    private final YoutubeDownloader downloader = new YoutubeDownloader();
    private final MetaData _song;


    public File audioDic = new File("Audios");
    public File imageDic = new File("Images");
    Format Aformat;
    private final VideoInfo video;

    public Downloader(MetaData song, File audioDic, File imageDic) {
        this._song = song;
        this.audioDic = audioDic;
        RequestVideoInfo request = new RequestVideoInfo(_song.getDownloadId());
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        this.video = response.data();
        List<AudioFormat> audioFormats = video.audioFormats();
        this.Aformat = audioFormats.get(1);
    }

    public Downloader(MetaData song) {
        this._song = song;
        this.audioDic = new File("Audios");
        RequestVideoInfo request = new RequestVideoInfo(_song.getDownloadId());
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        this.video = response.data();
        List<AudioFormat> audioFormats = video.audioFormats();
        this.Aformat = audioFormats.get(1);
    }


    public void downloadAudio() throws IOException, CannotWriteException, CannotReadException, TagException, InvalidAudioFrameException,  ReadOnlyFileException{
        RequestVideoFileDownload request3 = new RequestVideoFileDownload(Aformat)
                .callback(new YoutubeProgressCallback<File>() {
                    @Override
                    public void onDownloading(int progress) {
                        System.out.printf("Downloaded %d%%\n", progress);
                    }

                    @Override
                    public void onFinished(File videoInfo) {

                        System.out.println("Finished file: " + videoInfo);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error: " + throwable.getLocalizedMessage());
                    }
                })
                .saveTo(this.audioDic)
                .renameTo(_song.getSongName())
                .overwriteIfExists(false)
                .async();
        Response<File> response3 = downloader.downloadVideoFile(request3);
        File data = response3.data();
        File imageFile=  new ImageDownloader(_song.getImageUrl().getImageUrl(),imageDic).downloadImage();
        new SongDetailsAdder(data,imageFile, _song.getArtistName(), _song.getAlbumName()).add();
    }
}