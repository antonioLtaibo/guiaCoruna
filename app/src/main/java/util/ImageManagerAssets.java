package util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tributo on 15/11/14.
 */
public class ImageManagerAssets implements ImageManager{
    Context ctx;
    private String TAG = "ImageManagerAssets";

    public ImageManagerAssets(Context ctx){
        this.ctx=ctx;
    }




    public Bitmap getImage(String filename){
        InputStream is = null;
        boolean loadImageFailed = false;
        Bitmap image = null;
        try {
            is = ctx.getResources().getAssets().open(filename);
        } catch (IOException e) {
            e.printStackTrace();
            loadImageFailed = true;
        }

        if(loadImageFailed){
            try {
                is = ctx.getResources().getAssets().open("noPhoto.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(is !=null){
            image = BitmapFactory.decodeStream(is);
        }else{
            image = null;
        }
        return image;
    }

    public Bitmap getImageIcon(String filename){
        String realFilename = filename.substring(0,filename.lastIndexOf("."))+"_icon.png";
        InputStream is = null;

        Log.d(TAG,"Image icon filename is :"+realFilename);
        try {
            is = ctx.getResources().getAssets().open(realFilename);
        } catch (IOException e) {
            Log.d(TAG,"Image icon not found for :"+realFilename);
            Log.d(TAG,"Using whole image.");
            realFilename=filename;
        }

        return this.getImage(realFilename);

    }


}
