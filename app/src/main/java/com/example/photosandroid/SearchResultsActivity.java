package com.example.photosandroid;

import static com.example.photosandroid.R.id.*;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.io.File;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    private Album resultsAlbum;
    private ImageAdaptor adaptor;
    private  UserData userData;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);


        //read in the current album
        userData= UserData.getUserdata(getApplicationContext());


        resultsAlbum = (Album) getIntent().getSerializableExtra("resultsAlbum");

        //set add new photo action
        findViewById(R.id.BackButton).setOnClickListener(view -> back());

        //set recycler views data to the photo array

        adaptor = new ImageAdaptor(resultsAlbum.getPhotoList(),new ClickListener(){

            //set click listener for items in recycler view
            @Override
            public void click(int index) {

            }
        });

        RecyclerView recyclerView = findViewById(resultsList);


        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchResultsActivity.this));

    }


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


    public void back(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }


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
