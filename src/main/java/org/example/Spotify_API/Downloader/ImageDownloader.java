package org.example.Spotify_API.Downloader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloader {

    private final String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    private  File destinationFolderPath;


    public ImageDownloader(String imageUrl,File destinationFolderPath){
        this.imageUrl = imageUrl;

        this.destinationFolderPath = destinationFolderPath;

    }

    public ImageDownloader(String imageUrl){
        this.imageUrl = imageUrl;

    }


    public File downloadImage() throws IOException {
        // Extract the image's name from the URL
        String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        File destinationFile = new File(destinationFolderPath, imageName+".jpg");

        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();

        try (InputStream inputStream = new BufferedInputStream(connection.getInputStream());
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return destinationFile;
    }
}
