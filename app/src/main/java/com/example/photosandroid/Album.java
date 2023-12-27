package com.example.photosandroid;

import java.io.Serializable;
import java.util.ArrayList;

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

    /**
     * Album Constructor - constructs an Album with a given name.
     *
     * @param name The name of the album.
     */
    public Album(String name) {
        this.name = name;
        photoList = new ArrayList<Photo>();
    }

    /**
     * Gets all the photos in the album.
     *
     * @return an ArrayList of all the Photo objects in the album.
     */
    public ArrayList<Photo> getPhotos(){
        return photoList;
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
     * Gets the number of photos in the album.
     *
     * @return The number of photos of the album.
     */
    public int getSize() {
        return photoList.size();
    }

}
