package util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class ImageManagerInternal implements ImageManager{

    Context ctx;
    private String TAG = "ImageManagerInternal";

    public ImageManagerInternal(Context ctx){
        this.ctx=ctx;
    }

    @Override
    public Bitmap getImage(String filename) {
        Log.d(TAG,"Looking for file "+filename +" in Internal Storage");
        Bitmap image = null;

        ContextWrapper cw = new ContextWrapper(ctx);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory,filename);

        try {
            image = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            Log.d(TAG,"Image "+filename+" not found in Internal Storage.");
            return null;
        }
        return image;
    }

    @Override
    public Bitmap getImageIcon(String filename) {
        Log.d(TAG, "Looking for ICON file " + filename + " in Internal Storage");
        if (filename.equals("")){
            filename = "noPhoto.png";
        }
        String realFilename = filename.substring(0, filename.indexOf(".")) + "_icon.png";
        Bitmap image = null;
        boolean loadImageFailed = false;
        ContextWrapper cw = new ContextWrapper(ctx);

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File f = new File(directory,realFilename);
        try {
            image = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            loadImageFailed=true;
        }

        if(loadImageFailed){
            Log.d(TAG, "Image icon not found for :" + realFilename +" in Internal storage");
            Log.d(TAG,"Using whole image.");
            image = this.getImage(filename);
        }
        return image;
    }


    public String saveToInternalSorage(Bitmap bitmapImage, String imageName){
        ContextWrapper cw = new ContextWrapper(ctx);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,imageName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Log.d(TAG,"Saving image " + imageName + " to Internal storage.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

}
