package com.kawikaavilla.la_poro.helpers.firebase;

import com.google.firebase.database.DataSnapshot;
import java.util.List;

public interface IFirebaseHelper {
    void getCoinsData();
    void getFiatsData();
    void getCoinPrioritiesData();
    void getFiatPrioritiesData();
}
