package com.kawikaavilla.la_poro.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Response;
import com.google.android.gms.ads.MobileAds;
import com.kawikaavilla.common.Utils;
import com.kawikaavilla.la_poro.R;
import com.kawikaavilla.la_poro.datasources.TaskDataSource;
import com.kawikaavilla.la_poro.datasources.FiatDataSource;
import com.kawikaavilla.la_poro.helpers.logger.Logger;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {
    private static Logger logger;

    private static int successCounter = 0;
    //Number of required increments before moving forward

    @SuppressWarnings("FieldCanBeLocal")
    private static final int successRequirementCounter = 2;

    @SuppressWarnings("FieldCanBeLocal")
    private Context currentContext;

    protected static Reference<Context> currentContextRef;
    protected static Reference<SplashActivity> splashActivityRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        logger = new Logger(Utils.SPLASH_TAG);

        currentContext = this;
        currentContextRef = new WeakReference<>(currentContext);
        splashActivityRef = new WeakReference<>(SplashActivity.this);

        MobileAds.initialize(currentContext, (Utils.IS_DEV_MODE ? Utils.AD_MOB_APP_ID_DEV : Utils.AD_MOB_APP_ID));


        TaskDataSource.init(currentContext);
        FiatDataSource.init(currentContext);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentContextRef = null;
        splashActivityRef = null;
    }

    public static Response.Listener<String> successfulListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if (response.equals(Utils.SUCCESS_KEY)) {
                if (currentContextRef != null && splashActivityRef != null) {
                    successCounter += 1;
                    if (successCounter == successRequirementCounter) {
                        Intent intent = new Intent(currentContextRef.get(), PortfolioActivity.class);
                        currentContextRef.get().startActivity(intent);
                        successCounter = 0;
                        splashActivityRef.get().finish();
                    }
                }
            } else {
                logger.V("Splash initializing failed");
            }
        }
    };
}
