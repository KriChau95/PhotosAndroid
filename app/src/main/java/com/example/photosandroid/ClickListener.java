package com.example.photosandroid;

/**
 * Interface definition for a callback to be invoked when an item in a RecyclerView is clicked.
 */
public interface ClickListener {
        /**
         * Called when an item in a RecyclerView is clicked.
         *
         * @param index The index of the clicked item.
         */
        public void click(int index);

}
