package com.kawikaavilla.la_poro.activities;

import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.kawikaavilla.common.Utils;
import com.kawikaavilla.la_poro.R;
import com.kawikaavilla.la_poro.datasources.TaskDataSource;
import com.kawikaavilla.la_poro.datasources.CurrencyDataSource;
import com.kawikaavilla.la_poro.datasources.FiatDataSource;
import com.kawikaavilla.la_poro.helpers.logger.Logger;
import com.kawikaavilla.la_poro.models.Task;

public class EditPortfolioActivity extends AppCompatActivity {
    private Task currentCurrency;
    private EditText portfolioAmtET;

    private TextView originalPortfolioValueInBaseCurrTV;
    private EditText originalPortfolioValueInBaseCurrET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_portfolio_activity);

        final Logger logger = new Logger(Utils.EDIT_PORTFOLIO_TAG);
        final Utils utilsInstance = Utils.getInstance();

        currentCurrency = CurrencyDataSource.getCurrentCurrency();

        View view = findViewById(R.id.edit_portfolio_view);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        TextView nameTV = (TextView) toolbar.findViewById(R.id.title_tv);
        nameTV.setText(currentCurrency.getName());

        Button cancelBttn = (Button) toolbar.findViewById(R.id.left_bttn);
        cancelBttn.setText(R.string.cancel);

        Button saveBttn = (Button) toolbar.findViewById(R.id.right_bttn);
        saveBttn.setText(R.string.save);

        portfolioAmtET = (EditText) view.findViewById(R.id.portfolio_amt_et);
        portfolioAmtET.setText(Float.toString(currentCurrency.getPortfolioAmount()));

        originalPortfolioValueInBaseCurrTV = (TextView) view.findViewById(R.id.original_portfolio_value_in_base_curr_tv);
        originalPortfolioValueInBaseCurrET = (EditText) view.findViewById(R.id.original_portfolio_value_in_base_curr_et);

        if (currentCurrency instanceof Task) {
            Task coin = (Task) currentCurrency;
            String originalPortfolioValueInBaseCurrTVText = originalPortfolioValueInBaseCurrTV.getText().toString();
            originalPortfolioValueInBaseCurrTVText = String.format("%s (%s)", originalPortfolioValueInBaseCurrTVText, coin.getBaseCurrency());
            originalPortfolioValueInBaseCurrTV.setText(originalPortfolioValueInBaseCurrTVText);
            originalPortfolioValueInBaseCurrET.setText(utilsInstance.formatCurrencyValue(
                    coin.getIsBaseCurrencyFiat(),
                    coin.getOriginalPortfolioValueInBaseCurrency(),
                    Utils.EMPTY_STRING));
        } else if (currentCurrency instanceof Fiat){
            originalPortfolioValueInBaseCurrTV.setVisibility(View.GONE);
            originalPortfolioValueInBaseCurrET.setVisibility(View.GONE);
        }

        cancelBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrencyDataSource.setEditingCurrency(false);
                finish();
            }
        });

        saveBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentCurrency instanceof Task) {
                    String originalPortfolioValueInBaseCurr = originalPortfolioValueInBaseCurrET.getText().toString();
                    String portfolioAmt = portfolioAmtET.getText().toString();
                    if (!originalPortfolioValueInBaseCurr.equals(Utils.EMPTY_STRING)
                            && !portfolioAmt.equals(Utils.EMPTY_STRING)) {
                        Task tempCopyCoin = new Task((Task) currentCurrency);
                        tempCopyCoin.setOriginalPortfolioValueInBaseCurrency(Float.parseFloat(originalPortfolioValueInBaseCurr));
                        tempCopyCoin.setPortfolioAmount(Float.parseFloat(portfolioAmt));
                        TaskDataSource.updateCoinWithTempCoin(tempCopyCoin);
                    }
                } else if (currentCurrency instanceof Fiat){
                    String portfolioAmt = portfolioAmtET.getText().toString();
                    if (!portfolioAmt.equals(Utils.EMPTY_STRING)) {
                        Fiat tempCopyFiat = new Fiat((Fiat) currentCurrency);
                        tempCopyFiat.setPortfolioAmount(Float.parseFloat(portfolioAmt));
                        FiatDataSource.updateFiatWithTempFiat(tempCopyFiat);
                    }
                }

                CurrencyDataSource.setEditingCurrency(false);
                finish();
            }
        });

    }
}
