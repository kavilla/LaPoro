package com.kawikaavilla.common;

import android.content.Context;

/**
 * Created by Kawika on 3/17/2018.
 */

public class Utils implements IUtils {

    private static Utils instance = null;

    private Utils(){
    }

    public static synchronized Utils getInstance() {
        if (null == instance) instance = new Utils();
        return instance;
    }

    public String formatCurrencyValue (boolean isFiat, float value, String name) {
        String formattedValue = EMPTY_STRING;
        if (isFiat) {
            formattedValue = String.format(java.util.Locale.US, TWO_DECIMAL_FORMAT_STRING, value);
        }

        if (!name.equals(EMPTY_STRING)) {
            formattedValue = String.format("%s (%s)", formattedValue, name);
        }
        return formattedValue;
    }
}
