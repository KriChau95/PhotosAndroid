package com.example.photosandroid;


import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> albumList;
    private ArrayList<Album> myAlbums;
    private ArrayAdapter<String> adapter;

    private  UserData userData;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        userData= UserData.getUserdata(getApplicationContext());

        // Initialize the album list and adapter only if it's the first time
        if (albumList == null || albumList.isEmpty()) {
            albumList = new ArrayList<>();
            myAlbums = userData.getAlbumList();
            for (Album a : myAlbums) {
                albumList.add(a.getName());
            }
        }

        for (Album a : myAlbums) {
            System.out.println(a);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumList);

        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

        // Floating Action Button Click Listener
        findViewById(R.id.floatingActionButton).setOnClickListener(view -> showAlbumNameDialog());
        findViewById(R.id.searchButton).setOnClickListener(view -> searchPage());

        listView.setOnItemClickListener(
                (p, v, pos, id) -> openAlbum(pos)
        );

        // Check if the permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, so request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 69);
        }

    }

    private void searchPage(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void showAlbumNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Album Name");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_album, null);
        builder.setView(dialogView);

        final EditText albumNameEditText = dialogView.findViewById(R.id.albumNameEditText);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String albumName = albumNameEditText.getText().toString();

            for (int i = 0; i < userData.getAlbumList().size(); i++){
                if(userData.getAlbumList().get(i).getName().equals(albumName)){
                    Log.d("DuplicateCheck", "Album already exists: ");
                    return;
                }
            }

            // Add the album to the list and notify the adapter
            Album toAdd = new Album(albumName);
            //myAlbums.add(toAdd);
            userData.addAlbum(toAdd);
            UserData.store( getApplicationContext());
            albumList.add(albumName);
            adapter.notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        builder.show();
    }


    private void openAlbum(int pos) {

        Intent intent = new Intent(this, AlbumViewActivity.class);
        intent.putExtra("albumPos",pos);
        startActivity(intent);
    }



}
