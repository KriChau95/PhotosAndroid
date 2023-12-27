package com.example.photosandroid;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

/**
 * The {@code TagActivity} class is an activity class for adding and editing Tags for a specific Photo.
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 */
public class TagActivity extends AppCompatActivity {

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
        setContentView(R.layout.edit_tag);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        userData= UserData.getUserdata(getApplicationContext());

        userData= UserData.getUserdata(getApplicationContext());
        currAlbum =(Album)userData.getAlbumList().get((int)getIntent().getSerializableExtra("albumPos"));
        currPhoto=currAlbum.getPhoto((int)getIntent().getSerializableExtra("photoPos"));
        albumIndex = (int)getIntent().getSerializableExtra("albumPos");
        photoIndex = (int)getIntent().getSerializableExtra("photoPos");
        tags = currPhoto.getTagList();

        ArrayList<String> tagStrings = new ArrayList<String>();
        if (tags.size() > 0){
            for (Tag t: tags){
                tagStrings.add(t.toString());
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagStrings);

        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

        // Floating Action Button Click Listener
        findViewById(R.id.floatingActionButton).setOnClickListener(view -> AddTagDialog());
        findViewById(R.id.BackButton).setOnClickListener(view -> back());

        listView.setOnItemClickListener(
                (p, v, pos, id) -> askToDeleteTag(pos)
        );

    }

    /**
     * Ask the user if they want to delete a tag.
     *
     * @param pos The position of the tag in the list.
     */
    public void askToDeleteTag(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Tag");
        builder.setMessage("Do you want to delete this tag?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            deleteTag(pos);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * Delete a tag at a specific position.
     *
     * @param pos The position of the tag to be deleted.
     */
    private void deleteTag(int pos) {
        Tag tagToRemove = tags.get(pos);
        currPhoto.removeTag(tagToRemove);
        UserData.store(getApplicationContext());
        updateListView();
    }

    /**
     * Display a dialog for adding a new tag.
     */
    private void AddTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Tag Name");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_tag, null);
        builder.setView(dialogView);

        final EditText TagEditText = dialogView.findViewById(R.id.TagEditText);

        builder.setPositiveButton("Add as Person Tag", (dialog, which) -> {
            String personTag = TagEditText.getText().toString();

            Tag toAdd = new Tag("person", personTag);

            for (int i = 0; i < currPhoto.getTagList().size(); i++){
                if(currPhoto.getTagList().get(i).equals(toAdd)){
                    Log.d("DuplicateTag", "Tag already exists: ");
                    return;
                }
            }

            // Add the tag to the list and notify the adapter
            currPhoto.addTag(toAdd);
            UserData.store(getApplicationContext());
            updateListView();
        });

        builder.setNegativeButton("Add as Location Tag", (dialog, which) -> {
            String locationTag = TagEditText.getText().toString();

            Tag toAdd = new Tag("location", locationTag);

            for (int i = 0; i < currPhoto.getTagList().size(); i++){
                if(currPhoto.getTagList().get(i).equals(toAdd)){
                    Log.d("DuplicateTag", "Tag already exists: ");
                    return;
                }
            }

            // Add the album to the list and notify the adapter
            currPhoto.addTag(toAdd);
            UserData.store(getApplicationContext());
            updateListView();
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        builder.show();
    }

    /**
     * Update the list view with the current tags.
     */
    private void updateListView() {
        tags = currPhoto.getTagList();

        ArrayList<String> tagStrings = new ArrayList<>();
        for (Tag t: tags){
            tagStrings.add(t.toString());
        }

        adapter.clear();
        adapter.addAll(tagStrings);
        adapter.notifyDataSetChanged();
    }

    /**
     * Navigate back to the photo view activity.
     */
    private void back(){
        Intent intent = new Intent(this, photoViewActivity.class);
        intent.putExtra("albumPos",albumIndex);
        intent.putExtra("photoPos",photoIndex);
        startActivity(intent);
    }

}
