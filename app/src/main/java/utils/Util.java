package utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jibola on 1/5/2017.
 */

public class Util {

    public static float convertDpToPixel(float dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        int px = Math.round(dp
                * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static String streamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
