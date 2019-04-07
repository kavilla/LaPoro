package com.kawikaavilla.la_poro.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kawikaavilla.common.Utils;
import com.kawikaavilla.la_poro.R;
import com.kawikaavilla.la_poro.adapter.PortfolioAdapter;
import com.kawikaavilla.la_poro.datasources.TaskDataSource;
import com.kawikaavilla.la_poro.datasources.CurrencyDataSource;
import com.kawikaavilla.la_poro.datasources.FiatDataSource;
import com.kawikaavilla.la_poro.helpers.logger.Logger;
import com.kawikaavilla.la_poro.helpers.volley.VolleyHelper;
import com.kawikaavilla.la_poro.models.Task;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PortfolioActivity extends AppCompatActivity {
    private static Logger logger;
    private static VolleyHelper volleyHelper;

    private RecyclerView portfolioRecyclerView;
    private RecyclerView.Adapter portfolioAdapter;
    private RecyclerView.LayoutManager portfolioLayoutManager;

    private Button editBttn;

    @SuppressWarnings("FieldCanBeLocal")
    private Context currentContext;

    protected static Reference<Context> currentContextRef;
    protected static Reference<PortfolioActivity> portfolioActivityRef;

    private boolean isEditing = false;
    ArrayList<Task> currencyList;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_activity);

        logger = new Logger(Utils.PORTFOLIO_TAG);
        logger.V(TaskDataSource.coinArrayListToString());
        logger.V(FiatDataSource.fiatArrayListToString());

        currentContext = this;
        currentContextRef = new WeakReference<>(currentContext);
        portfolioActivityRef = new WeakReference<>(PortfolioActivity.this);

        View view = findViewById(R.id.portfolio_view);

        RelativeLayout adViewContainer = (RelativeLayout) view.findViewById(R.id.ad_view_container);
        AdView adView = new AdView(currentContext);
        adView.setAdUnitId((
                Utils.IS_DEV_MODE
                ? Utils.AD_MOB_PORTFOLIO_BANNER_ID_DEV
                : Utils.AD_MOB_PORTFOLIO_BANNER_ID
        ));
        adView.setAdSize(AdSize.SMART_BANNER);
        adViewContainer.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView titleTV = (TextView) toolbar.findViewById(R.id.title_tv);
        titleTV.setText(R.string.app_name);

        Button unusedBttn = (Button) toolbar.findViewById(R.id.left_bttn);
        unusedBttn.setText(Utils.EMPTY_STRING);

        portfolioRecyclerView = (RecyclerView) findViewById(R.id.portfolio_recycler_view);
        portfolioRecyclerView.setHasFixedSize(true);

        portfolioLayoutManager = new LinearLayoutManager(this);
        portfolioRecyclerView.setLayoutManager(portfolioLayoutManager);

        currencyList = new ArrayList<>();
        currencyList.addAll(TaskDataSource.getCoinArrayList());
        currencyList.addAll(FiatDataSource.getFiatArrayList());


        portfolioAdapter = new PortfolioAdapter(currentContextRef, currencyList);
        portfolioRecyclerView.setAdapter(portfolioAdapter);

        volleyHelper = new VolleyHelper(currentContext);
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                getCurrencyValues();
            }
        }, Utils.ZERO_INT, Utils.REFRESH_TIME);

        editBttn = (Button) toolbar.findViewById(R.id.right_bttn);
        editBttn.setText(R.string.edit);

        editBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditing = !isEditing;
                editBttn.setText((!isEditing ? R.string.edit : R.string.cancel));
                CurrencyDataSource.setEditingCurrency(isEditing);
                portfolioAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getCurrencyValues () {
        for (Task currency : currencyList) {
            if (currency instanceof Task) {
                final Task coin = (Task) currency;
                volleyHelper.getCoinValueInBaseCurrency(coin, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (currentContextRef != null && portfolioActivityRef != null) {
                            try {
                                JSONObject dataObj = (JSONObject) response.get(Utils.DATA_KEY);
                                String coinValueInBaseCurr = dataObj.getString(Utils.AMOUNT_KEY);
                                Task tempCopyCoin = new Task(coin);
                                tempCopyCoin.setCoinValueInBaseCurrency(Float.parseFloat(coinValueInBaseCurr));
                                TaskDataSource.updateCoinWithTempCoin(tempCopyCoin);
                                portfolioAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                logger.V(e.toString());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (currentContextRef != null && portfolioActivityRef != null) {
                            logger.V(error.toString());
                        }
                    }
                });

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isEditing = CurrencyDataSource.isEditingCurrency();
        editBttn.setText((!isEditing ? R.string.edit : R.string.cancel));
        getCurrencyValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (portfolioActivityRef != null) {
            String value = item.getTitle().toString();
            Intent intent = new Intent(portfolioActivityRef.get(), SettingsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Utils.VALUE_KEY, value);
            intent.putExtras(bundle);
            portfolioActivityRef.get().startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}


