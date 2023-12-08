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
 * @author Krishaan Chaudhary & Preston Clawson
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
     * Photo constructor - Constructs a Photo object from a JavaFX Image, extracting necessary data.
     *
     * @param i The JavaFX Image to create the photo from.
     */
//    public Photo(Image i){
//        caption = "";
//        tagList = new ArrayList<Tag>();
//        date = Calendar.getInstance();
//        date.set(Calendar.MILLISECOND, 0);
//        width = (int) i.getWidth();
//        height = (int) i.getHeight();
//        photoArray = new int[width][height];
//
//        PixelReader reader = i.getPixelReader();
//        if (reader != null){
//            for (int w = 0; w < width; w++) {
//                for (int h= 0; h < height; h++) {
//                    photoArray[w][h] = reader.getArgb(w, h);
//                }
//            }
//        }
//    }

    /**
     * Alternate Photo Constructor- constructs a Photo object from a File,
     * initializing the necessary data. This is done specifically to handle GIFs.
     *
     * @param f The File representing the GIF.
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

    public String getFilePath(){
        return imageFilePath;
    }
    public ArrayList<Tag> getTagList(){
        return tagList;
    }

    /**
     * Gets the JavaFX Image representation of this photo. 2 separate cases -
     * one for jpegs, png, and bitmap, the other for GIFS
     *
     * @return The JavaFX Image.
     */
//    public Image getImage(){
//        if (width != 0){
//            WritableImage result = new WritableImage(width, height);
//            PixelWriter writer = result.getPixelWriter();
//            for (int w = 0; w < width; w++){
//                for (int h = 0; h < height; h++){
//                    writer.setArgb(w, h, photoArray[w][h]);
//                }
//            }
//            return result;
//        } else {
//            return new Image(imageFilePath);
//        }
//
//    }

    /**
     * Gets the caption of this photo.
     *
     * @return The caption.
     */
    public String getCaption(){
        return caption;
    }

    /**
     * Gets the date when this photo was added to the app.
     *
     * @return The date.
     */
    public Calendar getDate(){
        return date;
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
