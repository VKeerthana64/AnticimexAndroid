package com.surveyor.app.surveyorapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by DELL on 23-Jan-19.
 */

public class ImageStorage {

    public static String saveToSdCard(Bitmap bitmap, String filename) {
        String stored = null;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConstants.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        File file = new File(mediaStorageDir.getAbsoluteFile(), filename);
        if (file.exists())
            return stored;

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    public static File getImage(String imagename) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConstants.IMAGE_DIRECTORY_NAME);

        File mediaImage = null;
        try {
            if (!mediaStorageDir.exists())
                return null;

            mediaImage = new File(mediaStorageDir.getPath() + File.separator + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }

    public static boolean checkifImageExists(String imagename) {
        Bitmap b = null;
        File file = ImageStorage.getImage(imagename);
        if (file == null){
            return false;
        }
        String path = file.getAbsolutePath();

        if (path != null)
            b = BitmapFactory.decodeFile(path);

        if (b == null || b.equals("")) {
            return false;
        }
        return true;
    }

}
