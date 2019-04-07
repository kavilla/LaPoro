package com.kawikaavilla.la_poro.helpers.volley;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by Kawika on 3/11/2018.
 */

public interface IVolleyHelper  {

    void getCoinValueInBaseCurrency(Task coin,
                                    JSONObject jsonRequest,
                                    Response.Listener<JSONObject> listener,
                                    Response.ErrorListener errorListener);
}
