package de.nihas101.imagesToPdfConverter;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import static de.nihas101.imagesToPdfConverter.Constants.DIRECTORY_IMAGE_PATH;

public class ImageMap {
    private final Map<String, Image> imageMap;

    private ImageMap(Map<String, Image> imageMap) {
        this.imageMap = imageMap;
    }

    public static ImageMap createImageMap(Map<String, Image> imageMap){
        return new ImageMap(imageMap);
    }

    public void setupImageMap(List<File> files, ProgressUpdater progressUpdater) {
        imageMap.clear();
        if(files.size() == 0) return;

        putImagesIntoMap(files, progressUpdater);
    }

    public void setupImageMap(List<File> files) {
        imageMap.clear();
        if(files.size() == 0) return;

        putImagesIntoMap(files, null);
    }

    private void putImagesIntoMap(List<File> files, ProgressUpdater progressUpdater){
        for (int index = 0; index < files.size(); index++) {
            if(files.get(index).isDirectory()) {
                File directoryImage = new File(DIRECTORY_IMAGE_PATH);
                putImageIntoMap(imageMap, directoryImage);
            }else{
                if(progressUpdater != null) progressUpdater.updateProgress(index);
                putImageIntoMap(imageMap, files.get(index));
            }
        }
    }

    private void putImageIntoMap(Map<String, Image> imageMap, File file){
        try {
            String url = file.toURI().toURL().toString();
            /*
             * Scale images for a smaller memory print.
             * Thanks: stackoverflow.com/questions/15088271/javafx-loading-images-and-memory-problems
             */
            imageMap.putIfAbsent(url, new Image(url, 100, 100, true, false));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public Image get(@NotNull String directoryImageString) {
        return imageMap.get(directoryImageString);
    }

    public void remove(String directoryImageString){
        imageMap.remove(directoryImageString);
    }
}
