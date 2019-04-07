package com.kawikaavilla.la_poro.datasources;

import com.kawikaavilla.la_poro.models.Task;

public class CurrencyDataSource {
    private static boolean editCurrency;
    private static Task currentCurrency;
    private static Task totalCurrency;

    public static boolean isEditingCurrency() {
        return editCurrency;
    }

    public static void setEditingCurrency(boolean editCurrency) {
        CurrencyDataSource.editCurrency = editCurrency;
    }

    public static Task getCurrentCurrency () {
        return currentCurrency;
    }

    public static void setCurrentCurrency (Task currency) {
        currentCurrency = currency;
    }
}
