package util;

import android.graphics.Bitmap;

/**
 * Created by tributo on 15/11/14.
 */
public interface ImageManager {

    public Bitmap getImage(String filename);
    public Bitmap getImageIcon(String filename);
}
