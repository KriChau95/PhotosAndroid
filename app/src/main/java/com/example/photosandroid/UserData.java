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

    private static UserData userdata;



    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    public void addAlbum(Album a) {
        albumList.add(a);
    }

    private UserData(Context context) {

        albumList = new ArrayList<>();

    }

    public static  UserData getUserdata(Context context){
        if (userdata ==null){
              userdata = new UserData(context);
              userdata = load(context);
        }
        return userdata;
    }

    public static synchronized void store( Context context) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(USERS_FILE, Context.MODE_PRIVATE));
            oos.writeObject(userdata);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static synchronized UserData load(Context context) {

        try {
            FileInputStream fileStream = context.openFileInput(USERS_FILE);
            ObjectInputStream ois = new ObjectInputStream(fileStream);
            Object object = ois.readObject();
            userdata = (UserData) object;
            ois.close();
        } catch (Exception e) {
            userdata = new UserData(context);
            store(context);
        }
return userdata;
    }
}
