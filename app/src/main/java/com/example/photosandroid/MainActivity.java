package com.example.photosandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private UserData userData = new UserData();
    private ArrayList<String> albumList;
    private ArrayList<Album> myAlbums;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // Load existing user data
        userData = UserData.load(getApplicationContext());

        // Initialize the album list and adapter only if it's the first time
        if (albumList == null || albumList.isEmpty()) {
            albumList = new ArrayList<>();
            myAlbums = userData.getAlbumList();
            for (Album a : myAlbums) {
                albumList.add(a.getName());
            }
        }

        Toast.makeText(this, "data List Length: " + userData.getAlbumList().size(), Toast.LENGTH_SHORT).show();

        for (Album a: myAlbums){
            System.out.println(a);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumList);

        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

        // Floating Action Button Click Listener
        findViewById(R.id.floatingActionButton).setOnClickListener(view -> showAlbumNameDialog());

        listView.setOnItemClickListener(
                (p,v,pos,id)->openAlbum(pos)
        );
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

            // Add the album to the list and notify the adapter
            Album toAdd = new Album(albumName);
            //myAlbums.add(toAdd);
            userData.addAlbum(toAdd);
            UserData.store(userData, getApplicationContext());
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
        intent.putExtra("album",myAlbums.get(pos));
        startActivity(intent);
}

}
