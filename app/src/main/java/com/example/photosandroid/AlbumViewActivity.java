package com.example.photosandroid;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlbumViewActivity extends AppCompatActivity {
    private Album currAlbum;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);
        currAlbum = (Album) getIntent().getSerializableExtra("album");

        TextView albumNameView = findViewById(R.id.albumNameTextView);
        albumNameView.setText(currAlbum.getName());
    }


}
