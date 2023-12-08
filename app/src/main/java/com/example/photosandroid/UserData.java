package com.example.photosandroid;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;

/**
 * The {@code UserData} class represents the data storage and management for user-related information,
 * including user accounts and their associated albums and photos.
 * <p>
 * @author Krishaan Chaudhary & Preston Clawson
 * This class implements the {@link Serializable} interface to allow for serialization.
 */
public class UserData implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Album> albumList;
    private final static String USERS_FILE = "userdata4.ser";

    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    public boolean hasAlbum(String name){
        for (Album a: albumList){
            if (a.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void addAlbum(Album a) {
        albumList.add(a);
    }

    public UserData() {
        albumList = new ArrayList<>();
    }

    public static void store(UserData userData, Context context) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(USERS_FILE, Context.MODE_PRIVATE));
            oos.writeObject(userData);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads UserData from a file. If the file does not exist, a new UserData object is created and stored.
     *
     * @return The loaded or newly created UserData object.
     */
    public static UserData load(Context context) {
        UserData userData = null;
        try {
            FileInputStream fileStream = context.openFileInput(USERS_FILE);
            ObjectInputStream ois = new ObjectInputStream(fileStream);
            Object object = ois.readObject();
            userData = (UserData) object;
            ois.close();
        } catch (Exception e) {
            userData = new UserData();
            store(userData, context);
        }
        return userData;
    }
}
