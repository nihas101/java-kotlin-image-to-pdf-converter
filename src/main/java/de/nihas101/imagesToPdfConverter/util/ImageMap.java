package de.nihas101.imagesToPdfConverter.util;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static de.nihas101.imagesToPdfConverter.util.Constants.DIRECTORY_IMAGE_PATH;
import static de.nihas101.imagesToPdfConverter.util.Constants.IMAGE_MAP_MAX_SIZE;

/**
 * A class for holding {@link Image}s
 */
public class ImageMap {
    /**
     * The {@link Map} mapping an absolute path to the corresponding {@link Image}
     */
    private final LinkedHashMap<String, Image> imageMap; // LinkedHashmap is used to keep entries ordered

    private ImageMap(LinkedHashMap<String, Image> imageMap) {
        this.imageMap = imageMap;
    }

    /**
     * The Factory method creating {@link ImageMap} instances
     * @param imageMap The {@link Map} that will hold the {@link Image}
     * @return The created {@link ImageMap} instance
     */
    public static ImageMap createImageMap(LinkedHashMap<String, Image> imageMap){
        return new ImageMap(imageMap);
    }

    public static ImageMap createImageMap(){
        return new ImageMap(new LinkedHashMap<>());
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
        for (int index = 0; index < files.size(); index++)
            putImageIntoMapIfFile(files.get(index), progressUpdater, index);
    }

    private void putImageIntoMapIfFile(File file, ProgressUpdater progressUpdater, int progress){
        if(file.isDirectory() && imageMap.size() < IMAGE_MAP_MAX_SIZE)
            putDirectoryImageIntoMap();
        else if(imageMap.size() < IMAGE_MAP_MAX_SIZE)
            putImageIntoMap(file);

        if(progressUpdater != null) progressUpdater.updateProgress(progress);
    }

    private void putDirectoryImageIntoMap(){
        File directoryImage = new File(DIRECTORY_IMAGE_PATH);
        if(!contains(directoryImage)) putImageIntoMap(directoryImage);
    }

    /**
     * Puts an {@link Image} into the map
     * @param file The {@link File} to load the {@link Image} from
     */
    private synchronized void putImageIntoMap(File file){
        try {
            String url = file.toURI().toURL().toString();
            /*
             * Scale images for a smaller memory print.
             * Thanks: stackoverflow.com/questions/15088271/javafx-loading-images-and-memory-problems
             */
            if(!imageMap.containsKey(url)) {
                if(imageMap.size() > IMAGE_MAP_MAX_SIZE) removeEldestImage();
                imageMap.put(url, new Image(url, 100, 100, true, false));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void removeEldestImage(){
        Iterator<Map.Entry<String,Image>> iterator = imageMap.entrySet().iterator();
        if(iterator.hasNext()) {
            String key = iterator.next().getKey();
            imageMap.remove(key);
        }
    }

    /**
     * @param file The {@link File} of the {@link Image}
     * @return The {@link Image} found under the given absolute path
     */
    @Nullable
    public synchronized Image get(@NotNull File file) throws MalformedURLException {
        String url = toUrlString(file);

        if(imageMap.containsKey(url))
            return imageMap.get(url);
        else{
            putImageIntoMap(file);
            return imageMap.get(url);
        }
    }

    private String toUrlString(File file) throws MalformedURLException {
        return file.toURI().toURL().toString();
    }

    /**
     * Removes the {@link Image} found under the given absolute path
     * @param absolutePathToImage The absolute Path to the {@link Image}
     */
    public void remove(String absolutePathToImage){
        imageMap.remove(absolutePathToImage);
    }

    public synchronized boolean contains(File file) {
        try {
            return imageMap.containsKey(file.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int size() { return imageMap.size(); }
}
