package com.example.photosandroid;

import com.example.photosandroid.Tag;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The {@code Photo} class represents a photo and the data associated
 * with it, such as a caption, list of tags, dimensions, and date of capture
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 * This class implements {@link Serializable} for serialization
 * */
public class Photo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String caption;
    private ArrayList<Tag> tagList;
    private int width;
    private int height;
    private int[][] photoArray;
    private Calendar date;
    private String imageFilePath;

    /**
     * Photo Constructor- constructs a Photo object from a File,
     * initializing the necessary data.
     *
     * @param f The File representing the Image.
     */
    public Photo(File f){
        caption = "";
        tagList = new ArrayList<Tag>();
        date = Calendar.getInstance();
        date.set(Calendar.MILLISECOND, 0);
        imageFilePath = f.getAbsolutePath();
    }

    /**
     * Gets the list of tags associated with this photo.
     *
     * @return The list of tags.
     */
    public ArrayList<Tag> getTagList(){
        return tagList;
    }

    /**
     * Gets the list of file path of this photo.
     *
     * @return The file path.
     */
    public String getFilePath(){
        return imageFilePath;
    }

    /**
     * Gets the caption of this photo.
     *
     * @return The caption.
     */
    public String getCaption(){
        return caption;
    }

    /**
     * Sets the caption for this photo.
     *
     * @param caption The new caption for the photo.
     */
    public void setCaption(String caption){
        this.caption = caption;
    }

    /**
     * Checks if this photo has a specific tag.
     *
     * @param t The tag to check for.
     * @return {@code true} if the tag is present, {@code false} otherwise.
     */
    public boolean hasTag(Tag t){
        for (Tag tag : tagList) {
            if (tag.equals(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a tag to the list of tags associated with this photo.
     *
     * @param t The tag to add.
     */
    public void addTag(Tag t){
        if (hasTag(t)){
            return;
        }
        tagList.add(t);
    }

    /**
     * Removes a tag from the list of tags associated with this photo.
     *
     * @param t The tag to remove.
     */
    public void removeTag(Tag t) {
        for (int i = tagList.size() - 1; i >= 0 ; i--) {
            if (tagList.get(i).equals(t)) {
                tagList.remove(i);
            }
        }
    }

}
