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

public class AlbumViewActivity extends AppCompatActivity {
    private Album currAlbum;
    private ImageAdaptor adaptor;

    private  UserData userData;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);



        //read in the current album
        userData= UserData.getUserdata(getApplicationContext());


        currAlbum = userData.getAlbumList().get((int) getIntent().getSerializableExtra("albumPos"));

        //set title for view
        TextView albumNameView = findViewById(albumNameTextView);
        albumNameView.setText(currAlbum.getName());
        //set add new photo action
        findViewById(R.id.addPhotoButton).setOnClickListener(view->addImage());
        findViewById(R.id.BackButton).setOnClickListener(view -> back());
        findViewById(R.id.RenameAlbumButton).setOnClickListener(view -> renameAlbumDialog());
        findViewById(R.id.DeleteAlbumButton).setOnClickListener(view -> deleteAlbum());
        //set recycler views data to the photo array

        RecyclerView recyclerView = findViewById(imageList);



        adaptor = new ImageAdaptor(currAlbum.getPhotoList(),new ClickListener(){
            //set click listener for items in recycler view
            @Override
            public void click(int index) {
                switchToPhotoView(index);
            }
        });

        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(AlbumViewActivity.this));

    }

    private void addImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && resultCode == RESULT_OK) {

            Uri photo = data.getData();
            String picturePath=getRealPathFromURI(photo,this);
            File file = new File(picturePath);
            currAlbum.addPhoto(new Photo(file));

            UserData.store(getApplicationContext());

            adaptor.notifyItemInserted(currAlbum.getSize()-1);

        }
    }

    public void deleteAlbum(){
        for (int i = userData.getAlbumList().size() - 1; i >= 0; i--){
            if(userData.getAlbumList().get(i).getName().equals(currAlbum.getName())){
                userData.deleteAlbum(i);
            }
        }
        UserData.store(getApplicationContext());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void renameAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter New Album Name");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_album, null);
        builder.setView(dialogView);

        final EditText albumNameEditText = dialogView.findViewById(R.id.albumNameEditText);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newAlbumName = albumNameEditText.getText().toString();

            // Add the album to the list and notify the adapter

            for (int i = 0; i < userData.getAlbumList().size(); i++){
                if(userData.getAlbumList().get(i).getName().equals(newAlbumName)){
                    return;
                }
            }

            for (int i = 0; i < userData.getAlbumList().size(); i++){
                if(userData.getAlbumList().get(i).getName().equals(currAlbum.getName())){
                    userData.getAlbumList().get(i).setName(newAlbumName);
                    TextView albumNameView = findViewById(albumNameTextView);
                    albumNameView.setText(newAlbumName);
                }
            }

            UserData.store(getApplicationContext());

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        builder.show();
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
    private void switchToPhotoView(int i){
        Intent intent = new Intent(this,photoViewActivity.class);
        intent.putExtra("albumPos",currAlbum.getCurrentIndex());

        intent.putExtra("photoPos",i);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adaptor.notifyDataSetChanged();
    }
}

//
//package com.example.photosandroid;
//
//import static com.example.photosandroid.R.id.*;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.photosandroid.Album;
//
//
//import java.util.ArrayList;
//import java.io.File;
//
//public class AlbumViewActivity extends AppCompatActivity {
//    private Album currAlbum;
//    private ImageAdaptor adaptor;
//
//    private ArrayList<Album> albumList;
//
//    private UserData userData;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.album_view);
//
//
//
//        //read in the current album
//        currAlbum = (Album) getIntent().getSerializableExtra("album");
//        userData = (UserData) getIntent().getSerializableExtra("data");
//        albumList = (ArrayList<Album>) getIntent().getSerializableExtra("albumList");
//        //set title for view
//        TextView albumNameView = findViewById(albumNameTextView);
//        albumNameView.setText(currAlbum.getName());
//        //set add new photo action
//        findViewById(R.id.addPhotoButton).setOnClickListener(view->addImage());
//        //set recyculer veiews data to the photo array
//
//        RecyclerView recyclerView = findViewById(imageList);
//        File file =new File("/storage/emulated/0/Download/images.jpeg");
//
//    currAlbum.addPhoto(new Photo(file));
//        adaptor = new ImageAdaptor(currAlbum.getPhotoList());
//
//        recyclerView.setAdapter(adaptor);
//        recyclerView.setLayoutManager(new LinearLayoutManager(AlbumViewActivity.this));
//
//        findViewById(R.id.RenameAlbumButton).setOnClickListener(view -> renameAlbumDialog());
//        findViewById(R.id.DeleteAlbumButton).setOnClickListener(view -> deleteAlbum());
//        findViewById(R.id.BackButton).setOnClickListener(view -> back());
//
//        Toast.makeText(this, "photos in album " + currAlbum.getSize(), Toast.LENGTH_SHORT).show();
//
//    }
//
//    private void addImage(){
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent,3);
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data!=null && resultCode == RESULT_OK) {
//
//            Uri photo = data.getData();
//            String picturePath=getRealPathFromURI(photo,this);
//            File file = new File(picturePath);
//            currAlbum.addPhoto(new Photo(file));
//
//            adaptor.notifyDataSetChanged();
//
//            UserData.store(userData,getApplicationContext());
//
//        }
//    }
//
//    private void renameAlbumDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Enter New Album Name");
//
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.add_album, null);
//        builder.setView(dialogView);
//
//        final EditText albumNameEditText = dialogView.findViewById(R.id.albumNameEditText);
//
//        builder.setPositiveButton("OK", (dialog, which) -> {
//            String newAlbumName = albumNameEditText.getText().toString();
//
//            // Add the album to the list and notify the adapter
//
//            for (int i = 0; i < userData.getAlbumList().size(); i++){
//                if(userData.getAlbumList().get(i).getName().equals(newAlbumName)){
//                    return;
//                }
//            }
//
//            for (int i = 0; i < userData.getAlbumList().size(); i++){
//                if(userData.getAlbumList().get(i).getName().equals(currAlbum.getName())){
//                    userData.getAlbumList().get(i).setName(newAlbumName);
//                    TextView albumNameView = findViewById(albumNameTextView);
//                    albumNameView.setText(newAlbumName);
//                }
//            }
//
//            UserData.store(userData, getApplicationContext());
//
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> {
//            dialog.cancel();
//        });
//
//        builder.show();
//    }
//
//    public void deleteAlbum(){
//        for (int i = userData.getAlbumList().size() - 1; i >= 0; i--){
//            if(userData.getAlbumList().get(i).getName().equals(currAlbum.getName())){
//                userData.deleteAlbum(i);
//            }
//        }
//        UserData.store(userData, getApplicationContext());
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
//
//    public void back(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
//
//    public String getRealPathFromURI(Uri contentURI, Activity context) {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        @SuppressWarnings("deprecation")
//        Cursor cursor = context.managedQuery(contentURI, projection, null,
//                null, null);
//        if (cursor == null)
//            return null;
//        int column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        if (cursor.moveToFirst()) {
//            String s = cursor.getString(column_index);
//            // cursor.close();
//            return s;
//        }
//        // cursor.close();
//        return null;
//    }
//}
