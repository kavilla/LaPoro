package com.kawikaavilla.la_poro.helpers.logger;

import android.util.Log;

import com.kawikaavilla.common.Utils;

/**
 * Logger to search logs easier.
 *
 * - Verbose mode logs only in dev mode
 */

public class Logger implements ILogger {

    private String origin = "QWERTY";
    private String tag;

    public Logger(String tag){
        this.tag = origin + " " + tag;
    }

    public void V (String msg) {
        if (!Utils.IS_DEV_MODE) return;
        Log.v(tag, msg);
    }
}
