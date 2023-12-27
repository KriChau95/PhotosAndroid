package com.example.photosandroid;

import static com.example.photosandroid.R.id.*;

import android.app.Activity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The {@code AlbumViewActivity} class is an activity class that is used for managing and viewing a specific album
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 */
public class AlbumViewActivity extends AppCompatActivity {

    private Album currAlbum;
    private ImageAdaptor adaptor;
    private  UserData userData;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Bitmap bmp;
    SharedPreferences sp;
    public static final int PERMISSION_REQUEST_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 1;
    private int currAlbumIndex;

    /**
     * Called when the activity is first created. Initializes the UI and sets up event listeners.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view);

        //read in the current album
        userData= UserData.getUserdata(getApplicationContext());

        currAlbum = userData.getAlbumList().get((int) getIntent().getSerializableExtra("albumPos"));
        currAlbumIndex = (int) getIntent().getSerializableExtra("albumPos");

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

    /**
     * Initiates the process of adding a new image to the current album.
     */
    private void addImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,3);
    }

    /**
     * Handles the result of selecting an image from the gallery and adds it to the current album.
     *
     * @param requestCode The request code passed to startActivityForResult.
     * @param resultCode  The result code indicating success or failure.
     * @param data        The Intent containing the result data.
     */
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

    /**
     * Deletes the current album and navigates back to the main activity.
     */
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

    /**
     * Navigates back to the main activity which displays a list of all the albums.
     */
    public void back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Displays a dialog for renaming the current album.
     */
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

    /**
     * Retrieves the real path of an image file from its URI.
     *
     * @param contentURI The URI of the image.
     * @param context    The context of the calling activity.
     * @return The real path of the image file.
     */
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
            return s;
        }
        return null;
    }
    /**
     * Switches to the PhotoViewActivity to view a specific photo in the current album.
     *
     * @param i The index of the photo to be viewed.
     */
    private void switchToPhotoView(int i){
        Intent intent = new Intent(this,photoViewActivity.class);
        intent.putExtra("albumPos",currAlbumIndex);
        intent.putExtra("photoPos",i);
        startActivity(intent);
    }

    /**
     * Called when the activity is resumed. Notifies the adapter to update the UI.
     */
    @Override
    protected void onResume() {
        super.onResume();
        adaptor.notifyDataSetChanged();
    }
}
