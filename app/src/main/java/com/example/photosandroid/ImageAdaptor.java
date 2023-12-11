package com.example.photosandroid;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;


public class ImageAdaptor extends RecyclerView.Adapter<ImageAdaptor.ViewHolder> {
    private ArrayList<Photo> localDataSet;


    private ClickListener listener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            imageView = (ImageView) view.findViewById(R.id.photoImageView);
            textView = (TextView) view.findViewById(R.id.pictureCaptionTextView);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView(){
            return imageView;
        }
    }


    public ImageAdaptor(ArrayList<Photo> dataSet,ClickListener listener) {

        localDataSet = dataSet;

        this.listener =listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_display, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet.get(position).getCaption());

        File file = new File(localDataSet.get(position).getFilePath());
        if(file.exists()) {

            viewHolder.getImageView().setImageURI(Uri.fromFile(file));
            viewHolder.getImageView().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    listener.click(position);
                }
            });

        }


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }


}