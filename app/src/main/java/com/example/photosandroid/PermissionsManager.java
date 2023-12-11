package com.example.photosandroid;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsManager {

    private static final int REQUEST_PERMISSION_CODE = 123;

    // Function to check if permissions are granted
    public static boolean checkPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Permission granted for read and write external storage
            int writePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int readMediaPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (writePermission != PackageManager.PERMISSION_GRANTED ||
                    readPermission != PackageManager.PERMISSION_GRANTED ||
                    readMediaPermission != PackageManager.PERMISSION_GRANTED) {
                // Request permissions
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }

    // Function to handle permission request results
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            // Check if all permissions are granted
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            // Handle the result of permission requests here
            if (!allPermissionsGranted) {
                // Permissions were not granted
                // You can notify the user or take necessary action here
            }
        }
    }
}
