package com.kawikaavilla.la_poro.helpers.volley;

/**
 * Created by Kawika on 3/10/2018.
 */

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kawikaavilla.common.Utils;

public class VolleyHelper implements IVolleyHelper {

    private final Context context;
    private final RequestQueue mRequestQueue;

    public VolleyHelper(Context c){
        context = c;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void getCoinValueInBaseCurrency(Task coin,
                                           JSONObject jsonRequest,
                                           Listener<JSONObject> listener,
                                           ErrorListener errorListener) {

//        if (coin != nil) {
//            if ([coin.endpointName isEqualToString:COINBASE]) {
//                NSString *baseUrl = [coin.endpointUrl stringByAppendingString:@"%@-%@/buy"];
//                NSString *coinValueUrl = [NSString stringWithFormat:baseUrl, coin.name, coin.baseCurrency];
//
//            [manager GET:coinValueUrl parameters:nil progress:nil success:^(NSURLSessionTask *task, id responseObject) {
//                    NSDictionary *dataObject = (NSDictionary *)[responseObject objectForKey:DATA_KEY];
//                    NSString * amount = (NSString *)[dataObject objectForKey:AMOUNT_KEY];
//                    Coin * copyCoin = [[CoinManager sharedCoinManager] copyCoin:coin];
//                    copyCoin.coinValueInBaseCurrency = [NSNumber numberWithFloat: [amount floatValue]];
//                [[CoinManager sharedCoinManager] updateCoinWithCoin:copyCoin];
//                    NSLog(@"%@ GET COIN VALUE IN BASE CURRENCY: %@: %@ %@", TAG, coin.name, coin.coinValueInBaseCurrency, coin.baseCurrency);
//                    //[[NSNotificationCenter defaultCenter] postNotificationName:GET_COINS_LISTENER_KEY object:nil];
//                } failure:^(NSURLSessionTask *operation, NSError *error) {
//                    NSHTTPURLResponse *response = error.userInfo[AFNetworkingOperationFailingURLResponseErrorKey];
//                    NSInteger statusCode = response.statusCode;
//                    NSLog(@"%@ GET COINS FAILED: STATUS %d", TAG, (int) statusCode);
//                    //[[NSNotificationCenter defaultCenter] postNotificationName:GET_COINS_LISTENER_FAILED_KEY object:nil];
//                }];
//            }
//        }

        if (coin != null) {
            if (coin.getEndpointName().equals(Utils.COINBASE)) {
                String baseUrl = coin.getEndpointUrl() + "/" + coin.getName() + "-" + coin.getBaseCurrency() + "/buy";
                JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, jsonRequest, listener, errorListener);
                mRequestQueue.add(objRequest);
            }
        }
    }

}
