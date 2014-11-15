package util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tributo on 15/11/14.
 */
public class ImageManagerExternal implements ImageManager{
    Context ctx;
    private String TAG = "ImageManagerExternal";

    public ImageManagerExternal(Context ctx){
        this.ctx=ctx;
    }

    @Override
    public Bitmap getImage(String filename) {
        Log.d(TAG,"Looking for file "+filename +" in External Storage");
        Bitmap image = null;
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root);
        File f = new File(myDir,filename);

        try {
            image = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            Log.d(TAG,"Image "+filename+" not found in External Storage.");
            return null;
        }
        return image;
    }

    @Override
    public Bitmap getImageIcon(String filename) {
        Log.d(TAG,"Looking for ICON file "+filename +" in External Storage");
        String realFilename = filename.substring(0,filename.indexOf("."))+"_icon.png";
        Bitmap image = null;
        boolean loadImageFailed = false;

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root);
        File f = new File(myDir,realFilename);


        try {
            image = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            loadImageFailed = true;
        }

        if(loadImageFailed){
            Log.d(TAG, "Image icon not found for :" + realFilename +" in External storage");
            Log.d(TAG,"Using whole image.");
            image = this.getImage(filename);
        }

        return image;
    }

    public void saveToExternalSorage(Bitmap bitmapImage, String imageName){

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root);
        File file = new File (myDir, imageName);
        if (file.exists())
            file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.d(TAG,"Saving image " + imageName + " to External storage.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
