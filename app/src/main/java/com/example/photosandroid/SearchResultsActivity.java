package com.example.photosandroid;

import static com.example.photosandroid.R.id.*;

import android.app.Activity;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

/**
 * The {@code SearchResultsActivity} class is an activity class for displaying the search results based on tag.
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 */
public class SearchResultsActivity extends AppCompatActivity {
    private Album resultsAlbum;
    private ImageAdaptor adaptor;
    private  UserData userData;

    /**
     * Called when the activity is first created. Initializes the UI and sets up event listeners.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        //read in the current album
        userData= UserData.getUserdata(getApplicationContext());

        resultsAlbum = (Album) getIntent().getSerializableExtra("resultsAlbum");

        //set add new photo action
        findViewById(R.id.BackButton).setOnClickListener(view -> back());

        adaptor = new ImageAdaptor(resultsAlbum.getPhotoList(),new ClickListener(){

            //set click listener for items in recycler view
            @Override
            public void click(int index) {

            }
        });

        //set recycler views data to the photo array
        RecyclerView recyclerView = findViewById(resultsList);

        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchResultsActivity.this));

    }


    /**
     * Handle the result of an activity for getting a new photo.
     *
     * @param requestCode The request code.
     * @param resultCode  The result code.
     * @param data        The intent data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && resultCode == RESULT_OK) {

            Uri photo = data.getData();
            String picturePath=getRealPathFromURI(photo,this);
            File file = new File(picturePath);
            resultsAlbum.addPhoto(new Photo(file));

            UserData.store(getApplicationContext());

            adaptor.notifyItemInserted(resultsAlbum.getSize()-1);

        }
    }

    /**
     * Navigate back to the SearchActivity.
     */
    public void back(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * Get the real path of a URI.
     *
     * @param contentURI The URI of the content.
     * @param context    The activity context.
     * @return The real path of the content URI.
     */
    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

}
