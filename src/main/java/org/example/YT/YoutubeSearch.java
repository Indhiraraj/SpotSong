package org.example.YT;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestSearchResult;
import com.github.kiulian.downloader.model.search.SearchResult;
import com.github.kiulian.downloader.model.search.SearchResultItem;
import com.github.kiulian.downloader.model.search.SearchResultVideoDetails;
import com.github.kiulian.downloader.model.search.field.*;
import org.example.Spotify_API.Models.MetaData;

import java.util.List;

public class YoutubeSearch {


    private MetaData _song;


    YoutubeDownloader downloader = new YoutubeDownloader();

    public YoutubeSearch(MetaData song) {
        this._song = song;


    }

    public void setDownloadId() {

        RequestSearchResult request = new RequestSearchResult(_song.getSongName()+" lyrics" + " by " + _song.getArtistName())
                .type(TypeField.VIDEO);

        SearchResult result = downloader.search(request).data();


        List<SearchResultItem> items = result.items();
        if (items.isEmpty()) {
            _song.setDownloadId("");
            return;
        }
        SearchResultItem item = items.get(0);
        switch (item.type()) {
            case VIDEO -> {
                _song.setDownloadId(item.asVideo().videoId());
                return;
            }
            case SHELF -> {
                for ( SearchResultVideoDetails video : item.asShelf().videos() ) {
                    _song.setDownloadId(video.videoId());
                    return;
                }
            }
        }
    }


}
