package com.example.photosandroid;

import com.example.photosandroid.Photo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The {@code Album} class represents a collection of photos, each with associated data.
 * It provides methods to manage and retrieve information about the photos in the album.
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 * This class implements the {@link Serializable} interface to allow for serialization.
 */
public class Album implements Serializable{

    private static final long serialVersionUID = 1L;
    private ArrayList<Photo> photoList;
    private String name;
    private Photo earliestPhoto;
    private Photo latestPhoto;
    private int currentIndex;


    /**
     * Gets the current index within the album.
     *
     * @return The current index.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }
    /**
     * Sets the current index within the album.
     *
     * @param i The new index to set.
     */
    public void setCurrentIndex(int i){
        currentIndex = i;
    }

    /**
     * Album Constructor - constructs an Album with a given name.
     *
     * @param name The name of the album.
     */
    public Album(String name) {
        this.name = name;
        photoList = new ArrayList<Photo>();
        earliestPhoto = null;
        latestPhoto = null;
    }

    /**
     * Finds the date range of the photos in the album, updating the earliest and latest photos.
     */
    public void findDateRange() {
        if (photoList.isEmpty()){
            earliestPhoto = null;
            latestPhoto = null;
            return;
        }
        earliestPhoto = photoList.get(0);
        latestPhoto = photoList.get(0);
        Calendar earliestDate = earliestPhoto.getDate();
        Calendar latestDate = latestPhoto.getDate();

        for (int i = 1; i < photoList.size(); i++){
            Photo currPhoto = photoList.get(i);
            Calendar thisDate = currPhoto.getDate();
            if (thisDate.before(earliestDate)){
                earliestDate = thisDate;
                earliestPhoto = currPhoto;
            }
            if (thisDate.after(latestDate)){
                latestDate = thisDate;
                latestPhoto = currPhoto;
            }
        }
    }

    /**
     * Gets the list of photos in the album.
     *
     * @return The list of photos.
     */
    public ArrayList<Photo> getPhotoList() {
        return photoList;
    }

    /**
     * Adds a photo to the album.
     *
     * @param p The photo to add.
     */
    public void addPhoto(Photo p) {
        photoList.add(p);
    }

    /**
     * Removes a photo at a specific index from the album.
     *
     * @param index The index of the photo to remove.
     */
    public void removeIndex(int index) {
        photoList.remove(index);
    }

    /**
     * Gets the name of the album.
     *
     * @return The name of the album.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the album.
     *
     * @param newName The new name for the album.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Gets a specific photo from the album based on its index.
     *
     * @param index The index of the photo to retrieve.
     * @return The photo at the specified index in the photoList.
     */
    public Photo getPhoto(int index) {
        return photoList.get(index);
    }

    /**
     * Gets the JavaFX Image representation of a specific photo in the album.
     *
     * @param index The index of the photo.
     * @return The JavaFX Image of the specified photo.
     */
//    public Image getImage(int index) {
//        return photoList.get(index).getImage();
//    }

    /**
     * Gets the number of photos in the album.
     *
     * @return The number of photos of the album.
     */
    public int getSize() {
        return photoList.size();
    }

    /**
     * Gets the date of the earliest photo in the album.
     *
     * @return The earliest date as a string, or an empty string if no photos are present.
     */
    public String getEarliestDate() {
        if (earliestPhoto != null) {
            return earliestPhoto.getDate().getTime().toString();
        } else {
            return "";
        }
    }
    /**
     * Gets the date of the latest photo in the album.
     *
     * @return The latest date as a string, or an empty string if no photos are present.
     */
    public String getLatestDate() {
        if (latestPhoto != null) {
            return latestPhoto.getDate().getTime().toString();
        } else {
            return "";
        }
    }

}
