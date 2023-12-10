package com.example.photosandroid;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.ArrayList;

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

    private String[] test = new String[]{"Apple", "apP", "pear", "PE", "Ball", "bALling"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_tags);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        userData= UserData.getUserdata(getApplicationContext());

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
//        userData= UserData.getUserdata(getApplicationContext());
//        currAlbum =(Album)userData.getAlbumList().get((int)getIntent().getSerializableExtra("albumPos"));
//        currPhoto=currAlbum.getPhoto((int)getIntent().getSerializableExtra("photoPos"));
//        albumIndex = (int)getIntent().getSerializableExtra("albumPos");
//        photoIndex = (int)getIntent().getSerializableExtra("photoPos");
//        tags = currPhoto.getTagList();
//
//        ArrayList<String> tagStrings = new ArrayList<String>();
//        if (tags.size() > 0){
//            for (Tag t: tags){
//                tagStrings.add(t.toString());
//            }
//        }
//
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagStrings);
//
//        ListView listView = findViewById(R.id.listview);
//        listView.setAdapter(adapter);
//
//        // Floating Action Button Click Listener
//        findViewById(R.id.floatingActionButton).setOnClickListener(view -> AddTagDialog());
        findViewById(R.id.BackButton).setOnClickListener(view -> back());
//
//        listView.setOnItemClickListener(
//                (p, v, pos, id) -> askToDeleteTag(pos)
//        );
//

    }

    private void back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}