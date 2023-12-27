package com.example.photosandroid;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The {@code ImageAdaptor} class is an adapter class for displaying a list of photos in a RecyclerView
 * <p>
 * @author Krishaan Chaudhary & Joshua Clayton
 */
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

        /**
         * Constructor for the ViewHolder.
         *
         * @param view The view associated with this ViewHolder.
         */
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            imageView = (ImageView) view.findViewById(R.id.photoImageView);
            textView = (TextView) view.findViewById(R.id.pictureCaptionTextView);
        }

        /**
         * Getter for the TextView in the ViewHolder.
         *
         * @return The TextView in the ViewHolder.
         */
        public TextView getTextView() {
            return textView;
        }

        /**
         * Getter for the ImageView in the ViewHolder.
         *
         * @return The ImageView in the ViewHolder.
         */
        public ImageView getImageView(){
            return imageView;
        }
    }

    /**
     * Constructor for the ImageAdaptor.
     *
     * @param dataSet   The dataset of photos to be displayed.
     * @param listener  The ClickListener for handling item click events.
     */
    public ImageAdaptor(ArrayList<Photo> dataSet,ClickListener listener) {
        localDataSet = dataSet;
        this.listener = listener;
    }

    /**
     * Creates a new ViewHolder when needed by the RecyclerView; creates new views, invoked by layout manager
     *
     * @param viewGroup The parent ViewGroup into which the new View will be added.
     * @param viewType  The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_display, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Replaces the contents of a view (invoked by layout manager) with the data of the corresponding photo.
     *
     * @param viewHolder The ViewHolder whose contents should be replaced.
     * @param position   The position of the item within the dataset.
     */
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

    /**
     * Returns the total number of items in the dataset (invoked by layout manager).
     *
     * @return The total number of items in the dataset.
     */
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    /**
     * Called when the adapter is attached to a RecyclerView.
     *
     * @param recyclerView The RecyclerView to which the adapter is attached.
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

}
