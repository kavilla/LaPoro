package com.kawikaavilla.la_poro.helpers.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kawikaavilla.common.Utils;
import com.kawikaavilla.la_poro.datasources.TaskDataSource;
import com.kawikaavilla.la_poro.datasources.FiatDataSource;
import com.kawikaavilla.la_poro.helpers.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper implements IFirebaseHelper {

    private static Logger logger;
    private final DatabaseReference coinsTable;
    private final DatabaseReference fiatsTable;
    private final DatabaseReference prioritiesTable;

    public FirebaseHelper (){
        logger = new Logger(Utils.FB_HELPER_TAG);
        DatabaseReference dbo = FirebaseDatabase.getInstance().getReference(Utils.FB_DBO);
        coinsTable = dbo.child(Utils.FB_COINS_TABLE);
        fiatsTable = dbo.child(Utils.FB_FIATS_TABLE);
        prioritiesTable = dbo.child(Utils.FB_PRIORITIES_TABLE);
    }

    public void getCoinsData () {
        if (coinsTable != null) {
            coinsTable.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<DataSnapshot> coinDataList = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        coinDataList.add(ds);
                    }

                    TaskDataSource.FBDataListener(coinDataList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    logger.V(databaseError.toString());
                }
            });
        }
    }

    public void getFiatsData () {
        if (fiatsTable != null) {
            fiatsTable.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<DataSnapshot> fiatDataList = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        fiatDataList.add(ds);
                    }

                    FiatDataSource.FBDataListener(fiatDataList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    logger.V(databaseError.toString());
                }
            });
        }
    }

    public void getCoinPrioritiesData () {
        if (prioritiesTable != null) {
            prioritiesTable.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Integer uid = ds.child(Utils.UID_KEY).getValue(Integer.class);
                        Integer priority = ds.child(Utils.VALUE_KEY).getValue(Integer.class);
                        if (uid == null || priority == null) continue;

                        Task coin = TaskDataSource.getCoinByUid(uid);
                        if (coin != null && coin.getPriority() == Utils.ZERO_INT) {
                            Task tempCopyCoin = new Task(coin);
                            tempCopyCoin.setPriority(priority);
                            TaskDataSource.updateCoinWithTempCoin(tempCopyCoin);
                        }
                    }

                    TaskDataSource.FBPrioritiesDataListener();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    logger.V(databaseError.toString());
                }
            });
        }
    }

    public void getFiatPrioritiesData () {
        if (prioritiesTable != null) {
            prioritiesTable.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Integer uid = ds.child(Utils.UID_KEY).getValue(Integer.class);
                        Integer priority = ds.child(Utils.VALUE_KEY).getValue(Integer.class);
                        if (uid == null || priority == null) continue;

                        Fiat fiat = FiatDataSource.getFiatByUid(uid);
                        if (fiat != null && fiat.getPriority() == Utils.ZERO_INT) {
                            Fiat tempCopyFiat = new Fiat(fiat);
                            tempCopyFiat.setPriority(priority);
                            FiatDataSource.updateFiatWithTempFiat(tempCopyFiat);
                        }
                    }

                    FiatDataSource.FBPrioritiesDataListener();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    logger.V(databaseError.toString());
                }
            });
        }
    }
}
