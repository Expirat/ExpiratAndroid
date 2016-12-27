package com.expirate.expirat.repository.groceries.local;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.expirate.expirat.model.GroceriesContract;
import com.expirate.expirat.utils.Constant;

public class GroceriesDBHelper extends SQLiteOpenHelper {

    public GroceriesDBHelper(Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GroceriesContract.Groceries.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(GroceriesContract.Groceries.DROP_QUERY);

        onCreate(db);
    }
}
