package com.example.photosandroid;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;

/**
 * The Singleton {@code UserData} class represents the data storage and management for user-related information,
 * including user accounts and their associated albums and photos.
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 * This class implements the {@link Serializable} interface to allow for serialization.
 */
public class UserData implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<Album> albumList;
    private final static String USERS_FILE = "userdata4.ser";
    private static UserData userdata;

    /**
     * Constructs a new instance of UserData with an empty album list.
     */
    private UserData() {
        albumList = new ArrayList<>();
    }

    /**
     * Gets the list of albums associated with this user data.
     *
     * @return The list of albums.
     */
    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    /**
     * Adds an album to the user's album list.
     *
     * @param a The album to be added.
     */
    public void addAlbum(Album a) {
        albumList.add(a);
    }

    /**
     * Deletes an album from the user's album list based on the provided index.
     *
     * @param index The index of the album to be deleted.
     */
    public void deleteAlbum(int index){
        albumList.remove(index);
    }

    /**
     * Retrieves the Singleton instance of UserData, creating it if necessary.
     *
     * @param context The application context.
     * @return The UserData instance.
     */
    public static UserData getUserdata(Context context){
        if (userdata ==null){
              userdata = new UserData();
              userdata = load(context);
        }
        return userdata;
    }

    /**
     * Stores the UserData to the specified context.
     *
     * @param context The application context.
     */
    public static synchronized void store( Context context) {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(USERS_FILE, Context.MODE_PRIVATE));
            oos.writeObject(userdata);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Loads UserData from the specified context, creating a new instance if it doesn't exist.
     *
     * @param context The application context.
     * @return The loaded or newly created UserData instance.
     */
    public static synchronized UserData load(Context context) {

        try {
            FileInputStream fileStream = context.openFileInput(USERS_FILE);
            ObjectInputStream ois = new ObjectInputStream(fileStream);
            Object object = ois.readObject();
            userdata = (UserData) object;
            ois.close();
        } catch (Exception e) {
            userdata = new UserData();
            store(context);
        }

    return userdata;

    }
}
