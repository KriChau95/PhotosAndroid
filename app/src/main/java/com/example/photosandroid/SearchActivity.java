package com.example.photosandroid;

import android.content.Intent;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

/**
 * The {@code SearchActivity} class is an activity class for searching for photos by tag.
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 */
public class SearchActivity extends AppCompatActivity {

    private ArrayList<String> tagList;
    private ArrayList<Album> myAlbums;
    private ArrayAdapter<String> adapter;
    private ArrayList<Tag> tags;
    private UserData userData;
    int albumIndex;
    int photoIndex;
    Album currAlbum;
    Photo currPhoto;

    /**
     * Called when the activity is first created. Initializes the UI and sets up event listeners.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_tags);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        userData= UserData.getUserdata(getApplicationContext());
        myAlbums = userData.getAlbumList();

        // an ArrayList to keep track of all tags set so far; useful for autocompleting search
        ArrayList<String> allTags = new ArrayList<String>();

        for (Album a : userData.getAlbumList()){
            for (Photo p: a.getPhotoList()){
                for (Tag t: p.getTagList()){
                    if (!allTags.contains(t.toString())){
                        allTags.add(t.toString());
                    }
                }
            }
        }

        String[] allTagArray = new String[allTags.size()];
        for (int i = 0; i < allTags.size(); i++){
            allTagArray[i] = allTags.get(i);
        }

        AutoCompleteTextView tag1 = findViewById(R.id.Tag1);
        AutoCompleteTextView tag2 = findViewById(R.id.Tag2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allTagArray);

        tag1.setAdapter(adapter);
        tag2.setAdapter(adapter);

        findViewById(R.id.BackButton).setOnClickListener(view -> back());
        findViewById(R.id.SearchTag1).setOnClickListener(view -> searchResults(0));
        findViewById(R.id.SearchTagAND).setOnClickListener(view -> searchResults(1));
        findViewById(R.id.SearchTagOR).setOnClickListener(view -> searchResults(2));

    }

    /**
     * Perform a search based on the selected tags and display the results.
     *
     * @param mode The search mode (0 for Tag 1, 1 for Tag 1 AND Tag 2, 2 for Tag 1 OR Tag 2).
     */
    public void searchResults(int mode){
        AutoCompleteTextView tag1 = findViewById(R.id.Tag1);
        AutoCompleteTextView tag2 = findViewById(R.id.Tag2);
        String targetTag1 = tag1.getText().toString().trim();
        String targetTag2 = tag2.getText().toString().trim();

        Album resultsAlbum = new Album("Results");

        if (mode == 0){ // Search Tag 1
            if (targetTag1 == null || targetTag1.equals("")){
                return;
            }
            for (Album a : myAlbums){
                for (Photo p: a.getPhotoList()){
                    for(Tag t: p.getTagList()){
                        if (t.toString().equals(targetTag1)){
                            resultsAlbum.addPhoto(p);
                        }
                    }
                }
            }
        } else if (mode == 2){ // Search Tag 1 OR Tag 2
            if (targetTag1 == null || targetTag2 == null || targetTag1.equals("") || targetTag2.equals("")){
                return;
            }
            for (Album album : myAlbums) {
                for (int j = 0; j < album.getSize(); j++) {
                    Photo photo = album.getPhoto(j);
                    boolean hasTargetTagOne = false;
                    boolean hasTargetTagTwo = false;
                    boolean photoAdded = false;
                    for (int k = 0; k < photo.getTagList().size(); k++) {
                        if (!photoAdded){
                            Tag tag = photo.getTagList().get(k);
                            if(targetTag1.equals(tag.toString())){
                                hasTargetTagOne = true;
                            }
                            if(targetTag2.equals(tag.toString())){
                                hasTargetTagTwo = true;
                            }
                            if (hasTargetTagOne || hasTargetTagTwo) {
                                resultsAlbum.addPhoto(photo);
                                photoAdded = true;
                            }
                        }
                    }


                }
            }

        } else { // Search Tag 1 AND Tag 2
            if (targetTag1 == null || targetTag2 == null || targetTag1.equals("") || targetTag2.equals("")){
                return;
            }
            for (Album album : myAlbums) {
                for (int j = 0; j < album.getSize(); j++) {
                    Photo photo = album.getPhoto(j);
                    boolean hasTargetTagOne = false;
                    boolean hasTargetTagTwo = false;
                    boolean photoAdded = false;
                    for (int k = 0; k < photo.getTagList().size(); k++) {
                        if (!photoAdded){
                            Tag tag = photo.getTagList().get(k);
                            if(targetTag1.equals(tag.toString())){
                                hasTargetTagOne = true;
                            }
                            if(targetTag2.equals(tag.toString())){
                                hasTargetTagTwo = true;
                            }
                            if (hasTargetTagOne && hasTargetTagTwo) {
                                resultsAlbum.addPhoto(photo);
                                photoAdded = true;
                            }
                        }
                    }

                }
            }
        }
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("resultsAlbum",resultsAlbum);
        startActivity(intent);
    }

    /**
     * Navigate back to the MainActivity.
     */
    private void back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
