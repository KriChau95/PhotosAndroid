package com.example.photosandroid;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

/**
 * The {@code photoViewActivity} class is an activity class for viewing and managing individual photos within an album.
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 */
public class photoViewActivity  extends AppCompatActivity {

     private UserData userData;
     Album currAlbum;
     Photo currPhoto;
     ImageView imageView;
     TextView captionView;
     private int currPhotoIndex;
     int currAlbumIndex;

    /**
     * Called when the activity is first created. Initializes the UI and sets up event listeners.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);
        //get data from previous activity

        userData = UserData.getUserdata(getApplicationContext());

        currPhotoIndex = (int) getIntent().getSerializableExtra("photoPos");
        currAlbumIndex = (int) getIntent().getSerializableExtra("albumPos");
        currAlbum = (Album) userData.getAlbumList().get(currAlbumIndex);
        currPhoto = currAlbum.getPhoto(currPhotoIndex);

        //declare changeable fields
        imageView = findViewById(R.id.imageView);
        captionView = findViewById(R.id.captionView);
        //set button action
        findViewById(R.id.CaptionButton).setOnClickListener((x) -> editCaption());

        findViewById(R.id.TagsButton).setOnClickListener((x) -> promptEditTags(currAlbumIndex, currPhotoIndex));

        findViewById(R.id.deleteButton).setOnClickListener((x) -> deletePhoto());
        findViewById(R.id.changeAlbumButton).setOnClickListener((x) -> promptSelectAlbum(currAlbumIndex));
        findViewById(R.id.rightButton).setOnClickListener((x) -> nextPhoto());
        findViewById(R.id.leftButton).setOnClickListener((x) -> previousPhoto());
        findViewById(R.id.BackButton).setOnClickListener((x) -> back());

        //set image view caption and album name
        TextView albumNameView = (TextView) findViewById(R.id.albumNameTextView);
        albumNameView.setText(currAlbum.getName());

        captionView.setText(currPhoto.getCaption());

        imageView.setImageURI(Uri.fromFile(new File(currPhoto.getFilePath())));
    }

    /**
     * Navigate back to the AlbumViewActivity.
     */
    private void back(){
        Intent intent= new Intent(this,AlbumViewActivity.class);
        intent.putExtra("albumPos",currAlbumIndex);
        startActivity(intent);
    }

    /**
     * Display the previous photo in the album.
     */
    private void previousPhoto() {
        if (currPhotoIndex-1<0) {
            back(); // if the back arrow is pressed on the first photo, the app will go back to AlbumViewActivity
        }else{
            currPhotoIndex-=1;
        }
        updateActivity();
    }

    /**
     * Display the next photo in the album.
     */
    private void nextPhoto() {
        if (currPhotoIndex+1>=currAlbum.getSize()) {
        currPhotoIndex=0;
        }else{
            currPhotoIndex+=1;
        }
        updateActivity();
    }

    /**
     * Prompt the user to select a different album for the current photo.
     *
     * @param albumIndex The index of the current album.
     */
    private void promptSelectAlbum(int albumIndex) {
        Intent intent= new Intent(this,SelectAlbumActivity.class);
        intent.putExtra("albumPos",albumIndex);
        intent.putExtra("photoPos",currPhotoIndex);
        startActivity(intent);
    }

    /**
     * Delete the current photo.
     */
    private void deletePhoto() {
        if(currAlbum.getSize()-1==0){
            currAlbum.getPhotos().remove(currPhoto);
            UserData.store(getApplicationContext());
            finish();
        }else{
            currAlbum.getPhotos().remove(currPhoto);
            UserData.store(getApplicationContext());
            previousPhoto();
        }
    }

    /**
     * Prompt the user to edit tags for the current photo.
     *
     * @param albumIndex The index of the current album.
     * @param photoIndex The index of the current photo.
     */
    private void promptEditTags(int albumIndex, int photoIndex) {
        Intent intent = new Intent(this,TagActivity.class);
        intent.putExtra("albumPos",albumIndex);
        intent.putExtra("photoPos",photoIndex);
        startActivity(intent);
    }

    /**
     * Prompt the user to edit the caption for the current photo.
     */
    private void editCaption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Caption");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_album, null);
        builder.setView(dialogView);

        final EditText newCaptionEditText = dialogView.findViewById(R.id.albumNameEditText);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newCaption = newCaptionEditText.getText().toString();

            currPhoto.setCaption(newCaption);
            UserData.store( getApplicationContext());
            captionView.setText(newCaption);

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        builder.show();
    }

    /**
     * Update the activity to display the current photo's information.
     */
    private void updateActivity(){
        currPhoto=currAlbum.getPhoto(currPhotoIndex);
       captionView.setText(currPhoto.getCaption());

       imageView.setImageURI(Uri.fromFile(new File(currPhoto.getFilePath())));

    }

}
