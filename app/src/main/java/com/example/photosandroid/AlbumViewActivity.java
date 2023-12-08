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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.io.File;

public class AlbumViewActivity extends AppCompatActivity {
    private Album currAlbum;
    private ImageAdaptor adaptor;

    private UserData userData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);



        //read in the current album
        currAlbum = (Album) getIntent().getSerializableExtra("album");
        userData = (UserData) getIntent().getSerializableExtra("data");
        //set title for view
        TextView albumNameView = findViewById(albumNameTextView);
        albumNameView.setText(currAlbum.getName());
        //set add new photo action
        findViewById(R.id.addPhotoButton).setOnClickListener(view->addImage());
        //set recyculer veiews data to the photo array

        RecyclerView recyclerView = findViewById(imageList);
        File file =new File("/storage/emulated/0/Download/images.jpeg");

    currAlbum.addPhoto(new Photo(file));
        adaptor = new ImageAdaptor(currAlbum.getPhotoList());

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

            UserData.store(userData,getApplicationContext());

            adaptor.notifyItemInserted(currAlbum.getSize()-1);

        }
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
