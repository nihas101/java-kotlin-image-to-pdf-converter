package de.nihas101.imagesToPdfConverter.util;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import static de.nihas101.imagesToPdfConverter.util.Constants.DIRECTORY_IMAGE_PATH;

/**
 * A class for holding {@link Image}s
 */
public class ImageMap {
    /**
     * The {@link Map} mapping an absolute path to the corresponding {@link Image}
     */
    private final Map<String, Image> imageMap;

    private ImageMap(Map<String, Image> imageMap) {
        this.imageMap = imageMap;
    }

    /**
     * The Factory method creating {@link ImageMap} instances
     * @param imageMap The {@link Map} that will hold the {@link Image}
     * @return The created {@link ImageMap} instance
     */
    public static ImageMap createImageMap(Map<String, Image> imageMap){
        return new ImageMap(imageMap);
    }

    /**
     * Loads {@link Image}s from the {@link List} and puts them into the {@link ImageMap#imageMap}
     * @param files The files to load {@link Image}s from
     * @param progressUpdater The {@link ProgressUpdater} to deliver updates
     */
    public void loadImages(List<File> files, ProgressUpdater progressUpdater) {
        imageMap.clear();
        if(files.size() == 0) return;

        putImagesIntoMap(files, progressUpdater);
    }

    public void loadImage(File file) {
        putImageIntoMap(file);
    }

    /**
     * Loads {@link Image}s from the {@link List} and puts them into the {@link ImageMap#imageMap}
     * @param files The files to load {@link Image}s from
     */
    public void loadImages(List<File> files) {
        if(files.size() == 0) return;

        putImagesIntoMap(files, null);
    }

    public void clearImages(){
        imageMap.clear();
    }

    /**
     * Puts {@link Image}s into the map
     * @param files The {@link File}s to load the {@link Image}s from
     * @param progressUpdater The {@link ProgressUpdater} to deliver updates
     */
    private void putImagesIntoMap(List<File> files, ProgressUpdater progressUpdater){
        for (int index = 0; index < files.size(); index++) {
            if(files.get(index).isDirectory()) {
                File directoryImage = new File(DIRECTORY_IMAGE_PATH);
                putImageIntoMap(directoryImage);
            }else{
                if(progressUpdater != null) progressUpdater.updateProgress(index);
                putImageIntoMap(files.get(index));
            }
        }
    }

    /**
     * Puts an {@link Image} into the map
     * @param file The {@link File} to load the {@link Image} from
     */
    private void putImageIntoMap(File file){
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

    /**
     * @param absolutePathToImage The absolute Path to the {@link Image}
     * @return The {@link Image} found under the given absolute path
     */
    @Nullable
    public Image get(@NotNull String absolutePathToImage) {
        return imageMap.get(absolutePathToImage);
    }

    /**
     * Removes the {@link Image} found under the given absolute path
     * @param absolutePathToImage The absolute Path to the {@link Image}
     */
    public void remove(String absolutePathToImage){
        imageMap.remove(absolutePathToImage);
    }

    public boolean contains(File file) {
        try {
            return imageMap.containsKey(file.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
