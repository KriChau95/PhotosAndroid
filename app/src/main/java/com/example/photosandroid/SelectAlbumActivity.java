package com.example.photosandroid;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SelectAlbumActivity extends AppCompatActivity {

    private UserData userData;
    private ArrayList<Album> myAlbums;
    private ArrayList<String> albumList;

    private ArrayAdapter<String> adapter;
    Album currAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        userData= UserData.getUserdata(getApplicationContext());


        if (albumList == null || albumList.isEmpty()) {
            albumList = new ArrayList<>();
            myAlbums = userData.getAlbumList();
            for (Album a : myAlbums) {
                albumList.add(a.getName());
            }
            albumList.remove(currAlbum.getName());
            if(albumList.size()<1) {
                Toast.makeText(this, "no other Albums exist", Toast.LENGTH_LONG).show();
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumList);

        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                (p, v, pos, id) -> moveAlbum(pos)
        );


    }

    private void moveAlbum(int index){
        finish();
    }
}
