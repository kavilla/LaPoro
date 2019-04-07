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
import com.kawikaavilla.la_poro.models.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Implements coin data source.
 */

public class TaskDataSource {

    private static DateFormat dateFormat;
    private static Logger logger;
    private static SharedPreferences taskPrefs;
    private static Map<Integer, Task> taskMap;
    private static FirebaseHelper fbHelper;

    @SuppressLint("UseSparseArrays")
    public static void init (Context context) {
        dateFormat = new SimpleDateFormat(Utils.DATE_FORMAT_STRING, Locale.US);
        logger = new Logger(Utils.COIN_DS_TAG);
        taskPrefs = context.getSharedPreferences(Utils.TASK_PREFERENCES_KEY, Context.MODE_PRIVATE);
        taskMap = new HashMap<>();

        String savedTasksString = taskPrefs.getString(Utils.COIN_NAMES_KEY, Utils.EMPTY_STRING);
        if (!savedTasksString.equals(Utils.EMPTY_STRING)) {
            String [] savedTasksNames = savedTasksString.split(Utils.BINDING_REGEX_STRING);
            for (String savedTaskName : savedTasksNames) {
                Task savedTask = loadTask(savedTaskName);
                taskMap.put(savedTask.getId(), savedTask);
            }
        }

        SplashActivity.successfulListener.onResponse(Utils.SUCCESS_KEY);
    }

    public static void updateCoinWithTempCoin (Task tempCoin) {
        Task coin = getCoinByUid(tempCoin.getUid());
        if (coin != null) {
            coin.setBaseCurrency(tempCoin.getBaseCurrency());
            coin.setIsBaseCurrencyFiat(tempCoin.getIsBaseCurrencyFiat());
            //TODO: update to handle non USD BASE CURRENCY and ACTUAL should use fiat object

            if (coin.getCoinValueInBaseCurrency() != tempCoin.getCoinValueInBaseCurrency()) {
                coin.setCoinValueInBaseCurrency(tempCoin.getCoinValueInBaseCurrency());
                float updatedPortfolioAmount = coin.getPortfolioAmount() * coin.getCoinValueInBaseCurrency();
                coin.setPortfolioValueInBaseCurrency(updatedPortfolioAmount);
            }

            if (coin.getPortfolioAmount() != tempCoin.getPortfolioAmount()) {
                coin.setPortfolioAmount(tempCoin.getPortfolioAmount());
                float updatedPortfolioAmount = coin.getPortfolioAmount() * coin.getCoinValueInBaseCurrency();
                coin.setPortfolioValueInBaseCurrency(updatedPortfolioAmount);
            }

            coin.setOriginalPortfolioValueInBaseCurrency(tempCoin.getOriginalPortfolioValueInBaseCurrency());
            coin.setPriority(tempCoin.getPriority());
            coin.setShowing(tempCoin.isShowing());

            // Checker to see if coin in coin array was actually updated
            if (Utils.IS_DEV_MODE) {
                Task foundCoin = getCoinByUid(coin.getUid());
                if (foundCoin != null) {
                    logger.V(foundCoin.toString());
                }
            }

            saveCoin(coin);
        }
    }

    public static ArrayList<Task> getCoinArrayList () { return new ArrayList<>(coinMap.values()); }

    public static Task getCoinByUid (int uid) { return coinMap.get(uid); }

    public static String coinArrayListToString () {
        StringBuilder sb = new StringBuilder();
        ArrayList<Task> coinArrayList = getCoinArrayList();
        for (int i = 0; i < coinArrayList.size(); i += 1) {
            String coinString = coinArrayList.get(i).toString() + Utils.NEW_LINE_CHARACTER;
            sb.append(coinString);
        }
        return sb.toString();
    }

    private static Task loadTask (String taskName) {
        Task task = null;
        Gson taskGSON = new Gson();
        String taskKey = taskName.toLowerCase() + Utils.TASK_KEY;
        String taskJSON = taskPrefs.getString(taskKey, Utils.EMPTY_STRING);
        if (!taskJSON.equals(Utils.EMPTY_STRING)) {
            task = taskGSON.fromJson(taskJSON, Task.class);
        }
        return task;
    }

    private static void saveTask (Task task) throws NullPointerException {
        Gson taskGSON = new Gson();
        String taskJSON = taskGSON.toJson(task);
        String taskKey = task.getName().toLowerCase() + Utils.TASK_KEY;
        taskPrefs.edit().putString(taskKey, taskJSON).apply();
    }
//
//- (Coin *) getCoinByName:(NSString *) name {
//        Coin * returnCoin = nil;
//        for (Coin * coin in coinArray) {
//            if ([coin.name isEqualToString: name]) {
//                returnCoin = coin;
//                break;
//            }
//        }
//        return returnCoin;
//    }

}