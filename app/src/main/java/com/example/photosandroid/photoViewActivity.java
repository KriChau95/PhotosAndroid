package com.example.photosandroid;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class photoViewActivity  extends AppCompatActivity {

     private  UserData userData;
     Album currAlbum;
     Photo currPhoto;
     
     ImageView imageView;
     
     TextView captionView;
     private int currPhotoIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);
        //get data from previous activity

        userData = UserData.getUserdata(getApplicationContext());

        currPhotoIndex = (int) getIntent().getSerializableExtra("photoPos");
        int currAlbumIndex = (int) getIntent().getSerializableExtra("albumPos");
        currAlbum = (Album) userData.getAlbumList().get(currAlbumIndex);
        currPhoto = currAlbum.getPhoto(currPhotoIndex);


        //declare changeable fields
        imageView = findViewById(R.id.imageView);
        captionView = findViewById(R.id.captionView);
        //set button action
        findViewById(R.id.CaptionButton).setOnClickListener((x) -> editCaption());

        findViewById(R.id.TagsButton).setOnClickListener((x) -> promptEditTags(currAlbumIndex, currPhotoIndex));

        findViewById(R.id.deleteButton).setOnClickListener((x) -> deletePhoto());
        findViewById(R.id.changeAlbumButton).setOnClickListener((x) -> promptSelectAlbum());
        findViewById(R.id.rightButton).setOnClickListener((x) -> nextPhoto());
        findViewById(R.id.leftButton).setOnClickListener((x) -> previousPhoto());
        //set image view caption and album name
        TextView albumNameView = (TextView) findViewById(R.id.albumNameTextView);
        albumNameView.setText(currAlbum.getName());

        captionView.setText(currPhoto.getCaption());

        imageView.setImageURI(Uri.fromFile(new File(currPhoto.getFilePath())));
    }

    private void previousPhoto() {
        if (currPhotoIndex-1<0) {
            currPhotoIndex=currAlbum.getSize();
        }else{
            currPhotoIndex-=1;
        }
        updateActivity();


    }

    private void nextPhoto() {

        if (currPhotoIndex+1>=currAlbum.getSize()) {
        currPhotoIndex=0;
        }else{
            currPhotoIndex+=1;
        }
        updateActivity();

    }

    private void promptSelectAlbum() {
    }

    private void deletePhoto() {
        if(currAlbum.getSize()-1==0){
            currAlbum.getPhotos().remove(currPhoto);
            UserData.store(getApplicationContext());

            finish();
        }else{
            currAlbum.getPhotos().remove(currPhoto);
            previousPhoto();

        }
    }


    private void promptEditTags(int albumIndex, int photoIndex) {
        Intent intent = new Intent(this,TagActivity.class);
        intent.putExtra("albumPos",albumIndex);
        intent.putExtra("photoPos",photoIndex);
        startActivity(intent);

    }

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

   private void updateActivity(){
        currPhoto=currAlbum.getPhoto(currPhotoIndex);
       captionView.setText(currPhoto.getCaption());

       imageView.setImageURI(Uri.fromFile(new File(currPhoto.getFilePath())));

    }

}
