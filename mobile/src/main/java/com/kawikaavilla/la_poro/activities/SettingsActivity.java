package com.kawikaavilla.la_poro.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.kawikaavilla.common.Utils;
import com.kawikaavilla.la_poro.R;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        View view = findViewById(R.id.settings_view);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView titleTV = (TextView) toolbar.findViewById(R.id.title_tv);
        titleTV.setText(R.string.settings);

        Button unusedLeftBttn = (Button) toolbar.findViewById(R.id.left_bttn);
        unusedLeftBttn.setText(Utils.EMPTY_STRING);

        Button closeBttn = (Button) toolbar.findViewById(R.id.right_bttn);
        closeBttn.setText(R.string.close);

        TextView informationTV = (TextView) view.findViewById(R.id.information_tv);

        HashMap<String, String> settingsMap = new HashMap<>();

        settingsMap.put(getString(R.string.about), getString(R.string.about_settings) + Utils.SPACE_STRING + Utils.VERSION_NUMBER);
        settingsMap.put(getString(R.string.terms_and_agreement), getString(R.string.terms_and_agreement_settings));
        settingsMap.put(getString(R.string.privacy_policy), getString(R.string.privacy_policy_settings));
        settingsMap.put(getString(R.string.acknowledgements), getString(R.string.acknowledgements_settings));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String value = bundle.getString(Utils.VALUE_KEY, Utils.EMPTY_STRING);
            informationTV.setText(settingsMap.get(value));
        }

        closeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
