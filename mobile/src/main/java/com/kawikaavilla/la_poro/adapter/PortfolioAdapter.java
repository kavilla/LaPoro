package com.kawikaavilla.la_poro.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.kawikaavilla.common.Utils;
import com.kawikaavilla.la_poro.R;
import com.kawikaavilla.la_poro.activities.EditPortfolioActivity;
import com.kawikaavilla.la_poro.datasources.CurrencyDataSource;
import com.kawikaavilla.la_poro.models.Task;

import java.lang.ref.Reference;
import java.util.ArrayList;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.Cell> {
    private ArrayList<Task> portfolioDataSet;
    private Reference<Context> contextRef;

    public static class Cell extends RecyclerView.ViewHolder {
        private View cellV;
        private TextView nameTV;
        private TextView amountTV;
        private TextView valueTV;
        private TextView diffTV;

        private Cell(View view) {
            super(view);
            cellV = view.findViewById(R.id.portfolio_adapter_cell_view);
            nameTV = (TextView) view.findViewById(R.id.portfolio_adapter_cell_name_tv);
            amountTV = (TextView) view.findViewById(R.id.portfolio_adapter_cell_amt_tv);
            valueTV = (TextView) view.findViewById(R.id.portfolio_adapter_cell_value_tv);
            diffTV = (TextView) view.findViewById(R.id.portfolio_adapter_cell_diff_tv);

        }
    }

    public PortfolioAdapter(Reference<Context> contextRef, ArrayList<Task> portfolioDataSet) {
        this.portfolioDataSet = portfolioDataSet;
        this.contextRef = contextRef;
    }

    @Override
    public PortfolioAdapter.Cell onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.portfolio_adapter_cell, parent, false);

        return new Cell(itemView);
    }

    @Override
    public void onBindViewHolder(Cell cell, int position) {
        Utils utilsInstance = Utils.getInstance();
        final Task currency = portfolioDataSet.get(position);
        cell.nameTV.setText(currency.getName());

        if (currency instanceof Task) {
            Task coin = (Task) currency;
            cell.amountTV.setText(Float.toString(currency.getPortfolioAmount()));

            cell.valueTV.setText(utilsInstance.formatCurrencyValue(
                    coin.getIsBaseCurrencyFiat(),
                    coin.getPortfolioValueInBaseCurrency(),
                    coin.getBaseCurrency()
            ));

            float diff = coin.getPortfolioValueInBaseCurrency() - coin.getOriginalPortfolioValueInBaseCurrency();

            cell.diffTV.setText(utilsInstance.formatCurrencyValue(
                    coin.getIsBaseCurrencyFiat(),
                    diff,
                    coin.getBaseCurrency()
            ));
            cell.diffTV.setTextColor((diff >= Utils.ZERO_INT ? Color.GREEN : Color.RED));
        } else if (currency instanceof Fiat) {
            Fiat fiat = (Fiat) currency;
            //cell.valueTV.setText(Float.toString(fiat.getValueInUSD()));
            cell.amountTV.setText(utilsInstance.formatCurrencyValue(
                    true, fiat.getPortfolioAmount(), fiat.getName()
            ));

            cell.valueTV.setVisibility(View.INVISIBLE);
            cell.diffTV.setVisibility(View.INVISIBLE);

        }

        cell.cellV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CurrencyDataSource.isEditingCurrency()) {
                    CurrencyDataSource.setCurrentCurrency(currency);
                    if (contextRef != null) {
                        Vibrator vibe = (Vibrator) contextRef.get().getSystemService(Context.VIBRATOR_SERVICE);
                        if (vibe != null) vibe.vibrate(Utils.VIBRATE_TIME);
                        Intent intent = new Intent(contextRef.get(), EditPortfolioActivity.class);
                        contextRef.get().startActivity(intent);
                    }
                }
            }
        });

        if (CurrencyDataSource.isEditingCurrency()) {
            Animation animShake = AnimationUtils.loadAnimation(this.contextRef.get(),
                    (position % 2 == 0 ? R.anim.shake : R.anim.shake_reverse));
            cell.cellV.startAnimation(animShake);
        }
    }

    @Override
    public int getItemCount() {
        return portfolioDataSet.size();
    }
}
