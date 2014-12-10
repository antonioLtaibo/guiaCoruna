package util;

import es.udc.psi14.grupal.guiacoruna.R;

/**
 * Created by tributo on 29/10/14.
 */
public class util {

    public static final String TAG_TYPE = "TYPE";
    public static final String TAG_ICON = "ICON";
    public static final String TAG_ID = "ID";
    public static final String TAG_URI = "/home/tony/AndroidStudioProjects/guiaCoruna/app/src/main/assets/";

    public static final String TYPE_RESTAURANT = "RESTAURANT";
    public static final String TYPE_MUSEUM = "MUSEUM";
    public static final String TYPE_MONUMENT= "MONUMENT";
    public static final String TYPE_NIGHT = "NIGHT";
    public static final String TYPE_HOTEL = "HOTEL";
    public static final String TYPE_SHOP = "SHOP";

    public static final int ICON_TYPE_RESTAURANT = R.drawable.restaurant;
    public static final int ICON_TYPE_MUSEUM = R.drawable.museum;
    public static final int ICON_TYPE_MONUMENT= R.drawable.monument;
    public static final int ICON_TYPE_NIGHT = R.drawable.bar;
    public static final int ICON_TYPE_HOTEL = R.drawable.hotel;
    public static final int ICON_TYPE_SHOP = R.drawable.tend;

    public static int findIconType(String type){
        if (type.equals("RESTAURANT"))
            return R.drawable.restaurant;
        if (type.equals("MUSEUM"))
            return R.drawable.museum;
        if (type.equals("MONUMENT"))
            return R.drawable.monument;
        if (type.equals("NIGHT"))
            return R.drawable.bar;
        if (type.equals("HOTEL"))
            return R.drawable.hotel;
        if (type.equals("SHOP"))
            return R.drawable.tend;
        return 0;
    }

}
