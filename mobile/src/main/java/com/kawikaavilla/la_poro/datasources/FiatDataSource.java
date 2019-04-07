package com.kawikaavilla.la_poro.datasources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.kawikaavilla.common.Utils;
import com.kawikaavilla.la_poro.activities.SplashActivity;
import com.kawikaavilla.la_poro.helpers.firebase.FirebaseHelper;
import com.kawikaavilla.la_poro.helpers.logger.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

/**
 * Implements Fiat data source.
 */

public class FiatDataSource {

    private static DateFormat dateFormat;
    private static Logger logger;
    private static SharedPreferences fiatPrefs;
    private static Map<Integer, Fiat> fiatMap;
    private static FirebaseHelper fbHelper;

    @SuppressLint("UseSparseArrays")
    public static void init (Context context) {
        dateFormat = new SimpleDateFormat(Utils.DATE_FORMAT_STRING, Locale.US);
        logger = new Logger(Utils.FIAT_DS_TAG);
        fiatPrefs = context.getSharedPreferences(Utils.FIAT_PREFERENCES_KEY, Context.MODE_PRIVATE);
        fiatMap = new HashMap<>();

        String savedFiatNamesString = fiatPrefs.getString(Utils.FIAT_NAMES_KEY, Utils.EMPTY_STRING);
        if (!savedFiatNamesString.equals(Utils.EMPTY_STRING)) {
            String [] savedFiatNames = savedFiatNamesString.split(Utils.BINDING_REGEX_STRING);
            for (String savedFiatName : savedFiatNames) {
                Fiat savedFiat = loadFiat(savedFiatName);
                fiatMap.put(savedFiat.getUid(), savedFiat);
            }
        }

        if (!isMostRecent()){
            fbHelper = new FirebaseHelper();
            fbHelper.getFiatsData();
        } else {
            SplashActivity.successfulListener.onResponse(Utils.SUCCESS_KEY);
        }

    }

    @SuppressWarnings("ConstantConditions")
    public static void FBDataListener (List<DataSnapshot> dataList) {
        for (DataSnapshot data : dataList) {
            String key = data.getKey();
            Integer uid = data.child(Utils.UID_KEY).getValue(Integer.class);
            if (uid != null && getFiatByUid(uid) == null) {
                try {
                    float valueInUSD = data.child(Utils.VALUE_IN_USD_KEY).getValue(Float.class);
                    boolean defaultIsShowing = data.child(Utils.IS_SHOWING_KEY).getValue(Boolean.class);
                    String messageName = key.toLowerCase() + Utils.MESSAGE;

                    Fiat newFiat = new Fiat(uid, key, valueInUSD, Utils.ZERO_FLOAT,
                            Utils.ZERO_INT, defaultIsShowing, messageName);

                    fiatMap.put(newFiat.getUid(), newFiat);
                    String savedFiatNamesString = fiatPrefs.getString(Utils.FIAT_NAMES_KEY, Utils.EMPTY_STRING);
                    String updatedSavedFiatNamesString = !savedFiatNamesString.equals(Utils.EMPTY_STRING) ?
                            savedFiatNamesString.concat(Utils.BINDING_REGEX_STRING).concat(key) :
                            key;
                    fiatPrefs.edit().putString(Utils.FIAT_NAMES_KEY, updatedSavedFiatNamesString).apply();
                } catch (NullPointerException e) {
                    logger.V("Null pointer exception getting fiat data");
                }
            }
        }

        fbHelper.getFiatPrioritiesData();
    }

    public static void FBPrioritiesDataListener () {
        fiatPrefs.edit().putString(Utils.MOST_RECENT_DATE_KEY, dateFormat.format(new Date())).apply();
        SplashActivity.successfulListener.onResponse(Utils.SUCCESS_KEY);
    }

    private static boolean isMostRecent() {
        boolean isMostRecentData;
        String mostRecentString = fiatPrefs.getString(Utils.MOST_RECENT_DATE_KEY, Utils.EMPTY_STRING);
        if (!mostRecentString.equals(Utils.EMPTY_STRING)) {
            try {
                Date mostRecentDate = dateFormat.parse(mostRecentString);
                Date currentDate = dateFormat.parse(dateFormat.format(new Date()));

                // isMostRecentData is means it got data today
                isMostRecentData = !currentDate.after(mostRecentDate);
            } catch (ParseException e) {
                isMostRecentData = false;
            }

        } else {
            isMostRecentData = false;
        }
        return isMostRecentData;
    }

    public static ArrayList<Fiat> getFiatArrayList () {
        return new ArrayList<>(fiatMap.values());
    }

    public static Fiat getFiatByUid (int uid) {
        return fiatMap.get(uid);
    }

    public static void updateFiatWithTempFiat (Fiat tempFiat) {
        Fiat fiat = getFiatByUid(tempFiat.getUid());
        if (fiat != null) {
            //TODO portfolioValueInUSD
            fiat.setValueInUSD(tempFiat.getValueInUSD());
            fiat.setPortfolioAmount(tempFiat.getPortfolioAmount());
            fiat.setPriority(tempFiat.getPriority());
            fiat.setShowing(tempFiat.isShowing());

            // Checker to see if fiat in fiat array was actually updated
            if (Utils.IS_DEV_MODE) {
                Fiat foundFiat = getFiatByUid(fiat.getUid());
                if (foundFiat != null) {
                    logger.V(foundFiat.toString());
                }
            }

            saveFiat(fiat);
        }
    }

    public static String fiatArrayListToString () {
        StringBuilder sb = new StringBuilder();
        ArrayList<Fiat> fiatArrayList = getFiatArrayList();
        for (int i = 0; i < fiatArrayList.size(); i += 1) {
            String fiatString = fiatArrayList.get(i).toString() + Utils.NEW_LINE_CHARACTER;
            sb.append(fiatString);
        }
        return sb.toString();
    }

    private static Fiat loadFiat (String fiatName) {
        Fiat fiat = null;
        Gson fiatGSON = new Gson();
        String fiatKey = fiatName.toLowerCase() + Utils.FIAT_KEY;
        String fiatJSON = fiatPrefs.getString(fiatKey, Utils.EMPTY_STRING);
        if (!fiatJSON.equals(Utils.EMPTY_STRING)) {
            fiat = fiatGSON.fromJson(fiatJSON, Fiat.class);
        }
        return fiat;
    }

    private static void saveFiat (Fiat fiat) throws NullPointerException {
        Gson fiatGSON = new Gson();
        String fiatJSON = fiatGSON.toJson(fiat);
        String fiatKey = fiat.getName().toLowerCase() + Utils.FIAT_KEY;
        fiatPrefs.edit().putString(fiatKey, fiatJSON).apply();
    }
}
