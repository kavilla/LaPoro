package com.kawikaavilla.common;

/**
 * Created by Kawika on 3/17/2018.
 */

public interface IUtils {

    String VERSION_NUMBER = "1.0.0";

    boolean IS_DEV_MODE = false;
    String PLAYGROUND = "Playground";

    // IMPORTANT DATA VALUES
    String AD_MOB_APP_ID = "ca-app-pub-9562342313813242~2841190187";
    String AD_MOB_APP_ID_DEV = "ca-app-pub-3940256099942544~3347511713";
    String AD_MOB_PORTFOLIO_BANNER_ID = "ca-app-pub-9562342313813242/5531766858";
    String AD_MOB_PORTFOLIO_BANNER_ID_DEV = "ca-app-pub-3940256099942544/6300978111";
    int REFRESH_TIME = 3000;
    int VIBRATE_TIME = 100;

    String FB_DBO = "dbo";
    String FB_COINS_TABLE = "Coins";
    String FB_FIATS_TABLE = "Fiats";
    String FB_PRIORITIES_TABLE = "Priorities";

    String COINBASE = "COINBASE";
    String COINBASE_BASE_URL = "https://api.coinbase.com/v2/prices/";

    // HELPFUL VALUES
    float ZERO_FLOAT = 0.0f;
    int ZERO_INT = 0;
    String YES_KEY = "YES";
    String EMPTY_STRING = "";
    String SPACE_STRING = " ";
    String DATE_FORMAT_STRING = "dd-MMM-yyyy";
    String BINDING_REGEX_STRING = ",";
    String TWO_DECIMAL_FORMAT_STRING = "%.2f";
    String NEW_LINE_CHARACTER = "\n";
    String SUCCESS_KEY = "SUCCESS";
    String FAILED_KEY = "FAILED";

    // COINS
    String BTC = "BTC";
    String LTC = "LTC";
    String ETH = "ETH";

    // FIATS
    String USD = "USD";

    // PROJECT COMMUNICATION KEYS
    String MESSAGE = "Message";
    String BTC_MESSAGE = "btcMessage";
    String LTC_MESSAGE = "ltcMessage";
    String ETH_MESSAGE = "ethMessage";
    String USD_MESSAGE = "usdMessage";
    String UPDATE_MESSAGE = "updateMessage";

    // LISTENER KEYS
    String GET_COINS_LISTENER_KEY = "";
    String GET_COINS_LISTENER_FAILED_KEY = "";

    // REGULAR KEYS
    String TASK_KEY = "Task";
    String COIN_NAMES_KEY = "coinNames";
    String BTC_COIN_KEY = "btcCoin";
    String LTC_COIN_KEY = "ltcCoin";
    String ETH_COIN_KEY = "ethCoin";
    String FIAT_KEY = "Fiat";
    String FIAT_NAMES_KEY = "fiatNames";
    String USD_FIAT_KEY = "usdFiat";
    String VALUE_KEY = "value";
    String CONTENT_KEY = "content";
    String DATA_KEY = "data";
    String AMOUNT_KEY = "amount";
    String UID_KEY = "uid";
    String NAME_KEY = "name";
    String BASE_CURRENCY_KEY = "baseCurrency";
    String IS_BASE_CURRENCY_FIAT_KEY = "isBaseCurrencyFiat";
    String COIN_VALUE_IN_BASE_CURRENCY_KEY = "coinValueInBaseCurrency";
    String PORTFOLIO_AMOUNT_KEY = "portfolioAmount";
    String PORTFOLIO_VALUE_IN_BASE_CURRENCY_KEY = "portfolioValueInBaseCurrency";
    String ORIGINAL_PORTFOLIO_VALUE_IN_BASE_CURRENCY_KEY = "originalPortfolioValueInBaseCurrency";
    String ENDPOINT_NAME_KEY = "endpointName";
    String ENDPOINT_URL_KEY = "endpointUrl";
    String PRIORITY_KEY = "priority";
    String IS_SHOWING_KEY = "isShowing";
    String MESSAGE_NAME_KEY = "messageName";
    String VALUE_IN_USD_KEY = "valueInUSD";

    // SHARED PREFERENCES KEY
    String TASK_PREFERENCES_KEY = "taskPreferences";

    // TAGS
    String COIN_DS_TAG = "CoinDS";
    String FIAT_DS_TAG = "FiatDS";
    String FB_HELPER_TAG = "FBHelper";
    String SPLASH_TAG = "Splash";
    String PORTFOLIO_TAG = "Portfolio";
    String EDIT_PORTFOLIO_TAG = "EditPortfolio";

    // ERROR STRINGS
    String CANNOT_INIT_CLASS = "Cannot initialize or instantiate class";

    // METHODS
    String formatCurrencyValue (boolean isFiat, float value, String name);
}
